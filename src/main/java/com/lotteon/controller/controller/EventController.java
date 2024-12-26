package com.lotteon.controller.controller;

import com.lotteon.config.MyUserDetails;
import com.lotteon.entity.member.AttendanceEvent;
import com.lotteon.entity.member.Customer;
import com.lotteon.repository.member.AttendanceEventRepository;
import com.lotteon.repository.member.CustomerRepository;
import com.lotteon.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/event")
@Controller
@RequiredArgsConstructor
@Log4j2
public class EventController {

    private final EventService eventService;
    private final AttendanceEventRepository attendanceEventRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/attendance")
    public String attendance(Model model, @RequestParam Long id) {
        System.out.println(id);
        String result = eventService.updateEvent(id);
        System.out.println("==================");
        System.out.println(result);
        Customer customer = customerRepository.findById(id).get();
        AttendanceEvent event = attendanceEventRepository.findByCustomer(customer).get();
        if(event.getAttendanceMiddleState()==1){
            model.addAttribute("middle",true);
        }
        if(event.getAttendanceState()==1){
            model.addAttribute("state",true);
        }
        int days = eventService.findEvent(result,id);
        System.out.println("==================");
        System.out.println(days);
        model.addAttribute("days", days);
        return "pages/event/attendance";
    }

    @GetMapping("/birth")
    public String birth(Model model) {
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memId = auth.getUser().getId();
        String result = "false";
        result = eventService.issueCoupon(memId);
        System.out.println(result);
        return "redirect:/index?birth="+result;
    }

    @GetMapping("/coupon")
    public String coupon(Model model) {

        return "pages/event/coupon";
    }
}
