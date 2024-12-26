package com.lotteon.controller.apicontroller;

import com.lotteon.dto.responseDto.PostAddressDto;
import com.lotteon.service.member.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
@Log4j2
public class ApiMyAddressController {

    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<?> postAddress(
            @RequestBody PostAddressDto dto
            ){
        addressService.insertNewAddress(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/address")
    public ResponseEntity<?> putAddress(
            @RequestBody PostAddressDto dto
    ){
        addressService.updateNewAddress(dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/address")
    public ResponseEntity<?> patchAddress(
            @RequestBody PostAddressDto dto
    ){
        addressService.updateAddressState(dto.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/address")
    public ResponseEntity<?> deleteAddress(
            @RequestParam Long id
    ){
        addressService.deleteAddress(id);

        return ResponseEntity.ok().build();
    }
}
