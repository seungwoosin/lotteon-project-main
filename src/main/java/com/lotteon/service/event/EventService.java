package com.lotteon.service.event;

import com.lotteon.config.MyUserDetails;
import com.lotteon.entity.member.AttendanceEvent;
import com.lotteon.entity.member.Customer;
import com.lotteon.entity.point.Coupon;
import com.lotteon.entity.point.CustomerCoupon;
import com.lotteon.entity.point.Point;
import com.lotteon.repository.member.AttendanceEventRepository;
import com.lotteon.repository.member.CustomerRepository;
import com.lotteon.repository.point.CouponRepository;
import com.lotteon.repository.point.CustomerCouponRepository;
import com.lotteon.repository.point.PointRepository;
import com.lotteon.service.member.CustomerService;
import com.lotteon.service.point.CouponService;
import com.lotteon.service.point.CustomerCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final AttendanceEventRepository attendanceEventRepository;
    private final PointRepository pointRepository;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final CustomerCouponService customerCouponService;
    private final CouponRepository couponRepository;
    private final CustomerCouponRepository customerCouponRepository;

    public String updateEvent(Long id) {
        System.out.println("여기 왜 안들어옴???");
        Customer customer = customerRepository.findById(id).get();
        AttendanceEvent event;
        if (attendanceEventRepository.findByCustomer(customer).isEmpty()) {
            event = this.insertEvent(customer);
            attendanceEventRepository.save(event);
        } else {
            event = attendanceEventRepository.findByCustomer(customer).get();
        }
        if(event.getAttendanceToday()==1){
            return "done";
        }
        if(event.getAttendanceSequence()==2){
            return "complete";
        }

        if(event.getAttendanceSequence()==1){
            event.updateEventReset();
            attendanceEventRepository.save(event);
        } else if(event.getAttendanceSequence()==0){
            event.updateEventIncrement();
            attendanceEventRepository.save(event);
        }
        if(event.getAttendanceDays()==7){
            event.updateEventComplete();
            attendanceEventRepository.save(event);
            return "coupon";
        }
        return "advance";

    }

    @Scheduled(cron = "0 52 11 * * ?")
    public void resetEventSequence(){
        List<AttendanceEvent> attendanceEvents = attendanceEventRepository.findAll();
        List<AttendanceEvent> updatedEvents = new ArrayList<>();
        for (AttendanceEvent event : attendanceEvents) {
            if (event.getAttendanceSequence() == 1) {
                event.updateEventSequenceZero();
                updatedEvents.add(event);
            } else if (event.getAttendanceSequence() == 0) {
                event.updateEventSequenceOne();
                updatedEvents.add(event);
            }
        }
        attendanceEventRepository.saveAll(updatedEvents);
    }

    private AttendanceEvent insertEvent(Customer customer) {
        return AttendanceEvent.builder()
                .attendanceSequence(0)
                .customer(customer)
                .attendanceDays(0)
                .attendanceState(0)
                .attendanceMiddleState(0)
                .attendanceToday(0)
                .build();
    }


    public int findEvent(String result, Long id) {
        Customer customer = customerRepository.findById(id).get();
        AttendanceEvent newEvent = attendanceEventRepository.findByCustomer(customer).get();
        if(result.equals("done")){
            AttendanceEvent event = attendanceEventRepository.findByCustomer(customer).get();
            int count = event.getAttendanceDays();
            return count;
        }
        if(result.equals("complete")){
            this.updateCustomerPoint(100);
            return 7;
        } else if(result.equals("coupon")){
            customerCouponService.postCustCoupon((long)6);
            newEvent.updateEventSequenceEnd();
            attendanceEventRepository.save(newEvent);
            return 7;
        } else {
            AttendanceEvent event = attendanceEventRepository.findByCustomer(customer).get();
            int count = event.getAttendanceDays();

            this.updateCustomerPointOrCoupon(count,id);
            return count;
        }

    }

    private void updateCustomerPointOrCoupon(int count, Long id) {
        Customer customer = customerRepository.findById(id).get();
        AttendanceEvent event = attendanceEventRepository.findByCustomer(customer).get();
        if(count==5){
            if(event.getAttendanceMiddleState()==0){
                this.updateCustomerPoint(2000);
                event.updateEventMiddleState();
                attendanceEventRepository.save(event);
            } else {
                this.updateCustomerPoint(100);
            }
        } else {
            this.updateCustomerPoint(100);
        }
    }

    private void updateCustomerPoint(int value) {
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Customer customer = auth.getUser().getCustomer();

        LocalDate today = LocalDate.now().plusMonths(2);

        Point point = Point.builder()
                .pointVar(value)
                .pointType(1)
                .pointEtc("출석이벤트")
                .customer(customer)
                .pointExpiration(today)
                .build();
        pointRepository.save(point);

        int points = customerService.updateCustomerPoint(customer);
        customer.updatePoint(points);
        customerRepository.save(customer);
    }

    @Cacheable(value = "couponCache", key = "'birth_' + #id", cacheManager = "cacheManager")
    public String issueCoupon(Long id) {
        Customer customer = customerRepository.findById(id).get();

        Optional<Coupon> coupon = couponRepository.findById((long)7);
        if(coupon.isEmpty()){
            return "false";
        }

        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .couponState(1)
                .coupon(coupon.get())
                .customer(customer)
                .couponExpiration(LocalDate.now().plusDays(coupon.get().getCustomerCouponExpiration()-1))
                .build();

        customerCouponRepository.save(customerCoupon);
        return "true";
    }
}
