package com.mbc.day05.service;

import com.mbc.day05.domain.Product;
import com.mbc.day05.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

   /*
    [생성자 주입을 사용하는 이유]

    1. 의존성을 강제할 수 있음 (Null 방지)
      @Component
      public class OrderService {
        @Autowired
        private OrderRepository orderRepository; // 만약 OrderRepository가 Bean이 아니면 `null`이 주입됨 😱

        public void processOrder() {
            orderRepository.save(new Order()); // NullPointerException 발생 가능! 😭
        }
     }

    🚨 문제점: @Autowired 필드 주입은 null이 주입되더라도 애플리케이션이 실행될 수 있음.
        그리고 나중에 processOrder() 같은 메서드에서 orderRepository를 사용하려고 할 때 NullPointerException이 발생할 수 있음.
        런타임(실행 중)에야 문제를 발견함 → 문제를 미리 잡지 못하고, 예상치 못한 순간에 에러 발생!

    ✅ 반면, 생성자 주입은 OrderRepository가 Bean으로 등록되지 않으면 Spring이 아예 실행되지 않도록 막아버림!
    ✅ 즉, 애플리케이션 실행 전에 미리 문제를 감지할 수 있어서 더 안전함!

    2. 불변성 유지 (한 번 정해지면 못 바꿈)
      👉 final 키워드를 사용하면 객체가 만들어진 이후에는 값이 바뀌지 않음
      👉 @Autowired 필드 주입 방식에서는 값을 바꿀 수도 있어서 실수할 가능성이 있음!
        🔴 필드 주입(@Autowired)은 나중에 값이 바뀔 수도 있어서 위험!
        ✅ 생성자 주입을 쓰면, 한 번 값이 정해지면 바꿀 수 없어서 안전함!
    3. 코드가 더 간결해짐 (@RequiredArgsConstructor 사용 가능)
    👉 Lombok의 @RequiredArgsConstructor를 사용하면 생성자 자동 생성 가능!
    👉 코드가 짧아지고, 관리하기 편해짐! ✅
   */

    // 생성자 주입을 쓰는 이유 : 불변성 유지, Null 방지
    //final 키워드를 사용하면 해당 필드는 생성자에서 한 번만 할당될 수 있고 이후 변경될 수 없습니다.
    //이는 의존성이 주입된 후 변경될 가능성을 원천 차단하여 코드의 안정성을 높여줌

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    public Product getProductById(Long productId) {
        return productMapper.getProductById(productId);
    }

    public void saveProduct(Product product) {
        if (product.getProductId() == null) {
            productMapper.insertProduct(product);
        } else {
            productMapper.updateProduct(product);
        }
    }

    public void deleteProduct(Long productId) {
        productMapper.deleteProduct(productId);
    }
}

