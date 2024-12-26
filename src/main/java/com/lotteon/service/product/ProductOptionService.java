package com.lotteon.service.product;

import ch.qos.logback.classic.Logger;
import com.lotteon.dto.requestDto.PostProductOptionDTO;
import com.lotteon.dto.responseDto.GetOption1Dto;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductOption;
import com.lotteon.repository.product.ProductOptionRepository;
import com.lotteon.repository.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<GetOption1Dto> findByProdId(long prodId) {
        Optional<Product> product = productRepository.findById(prodId);
        List<ProductOption> options = productOptionRepository.findAllByProductAndOptionState(product.get(),0);
        List<GetOption1Dto> variableOptions = new ArrayList<>();
        if(options.get(0).getOptionName().equals("옵션없음")){
            GetOption1Dto getOption1Dto = GetOption1Dto.builder()
                    .optionId(options.get(0).getId())
                    .optionName(options.get(0).getOptionName())
                    .optionStock(options.get(0).getStock())
                    .type("noOption")
                    .build();
            variableOptions.add(getOption1Dto);
            return variableOptions;
        } else {
            List<GetOption1Dto> dtos = options.stream()
                    .collect(Collectors.toMap(
                            ProductOption::getOptionValue, // 중복 기준: optionValue
                            ProductOption::toGetOption1Dto, // 맵의 값으로 변환
                            (existing, replacement) -> existing // 중복 시 기존 값 유지
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            return dtos;
        }
    }

    public List<GetOption1Dto> findByOptionValue(String optionValue, Long prodId) {
        Optional<Product> product = productRepository.findById(prodId);
        List<ProductOption> options = productOptionRepository.findAllByProductAndOptionValue(product.get(),optionValue);
        List<GetOption1Dto> dtos = options.stream().map(v->v.toGetOption2Dto()).collect(Collectors.toList());

        return dtos;
    }


    public List<PostProductOptionDTO> findOptionByProduct(long prodId) {
        List<ProductOption> options = productOptionRepository.findByProductId(prodId);

        List<PostProductOptionDTO> optionDtos = new ArrayList<>();
        for(ProductOption option : options){
            PostProductOptionDTO optionDTO = modelMapper.map(option, PostProductOptionDTO.class);
            optionDtos.add(optionDTO);
        }
        return optionDtos;
    }

    public void insertProdOption(PostProductOptionDTO optionDTO) {

        Optional<Product> opt = productRepository.findById(optionDTO.getProductId());

        Product product = null;
        if (opt.isPresent()) {
            product = opt.get();
        }

        ProductOption productOption = ProductOption.builder()
                .product(product)
                .optionName(optionDTO.getOptionName())
                .optionValue(optionDTO.getOptionValue())
                .optionName2(optionDTO.getOptionName2())
                .optionValue2(optionDTO.getOptionValue2())
                .optionName3(optionDTO.getOptionName3())
                .optionValue3(optionDTO.getOptionValue3())
                .additionalPrice(optionDTO.getAdditionalPrice())
                .stock(optionDTO.getStock())
                .build();

        productOptionRepository.save(productOption);

    }

    public void updateProdOption(PostProductOptionDTO optionDTO){
        ProductOption productOption = productOptionRepository.findById(optionDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Entity not found"));;
        productOption.updateOption(optionDTO);
    }

    public void updateProdOptionState(long optionId, String type){
        ProductOption productOption = productOptionRepository.findById(optionId).orElseThrow(() -> new EntityNotFoundException("Entity not found"));;
        if(type.equals("delete")){
            productOption.updateOptionState(type);
        }else if(type.equals("cancel")){
            productOption.updateOptionState(type);
        }
    }

}
