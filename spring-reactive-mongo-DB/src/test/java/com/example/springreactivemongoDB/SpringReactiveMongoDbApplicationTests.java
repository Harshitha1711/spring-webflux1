package com.example.springreactivemongoDB;

import com.example.springreactivemongoDB.controller.ProductController;
import com.example.springreactivemongoDB.dto.ProductDto;
import com.example.springreactivemongoDB.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoDbApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
    private ProductService productService;


	@Test
	public void addProductTest(){

		Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("102","mobile",1,90000.0));
		when(productService.saveProduct(productDtoMono))
				.thenReturn(productDtoMono);

		webTestClient.post().uri("/products")
				.body(Mono.just(productDtoMono),ProductDto.class)
				.exchange()
				.expectStatus().isOk(); //200
	}

	@Test
	public void getProductsTest(){
		Flux<ProductDto> productDtoFlux=Flux.just(new ProductDto("103","TV",2,2000),
				new ProductDto("104","watch",3,4000));
		when(productService.getProducts())
				.thenReturn(productDtoFlux);

		Flux<ProductDto>  responseBody = webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("103","TV",2,2000))
				.expectNext(new ProductDto("104","watch",3,4000))
				.verifyComplete();

	}

	@Test
	public void getProductTest(){
		Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("102","mobile",1,20000));
		when(productService.getProduct(any()))
				.thenReturn(productDtoMono);


		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/102")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p->p.getName().equals("mobile"))
				.verifyComplete();
	}


	@Test
	public void updateProduct(){
           Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102","mobile",3,20000));
		   when(productService.updateProduct(productDtoMono,"102"))
				   .thenReturn(productDtoMono);


		webTestClient.put().uri("/products/update/102")
				.body(Mono.just(productDtoMono),ProductDto.class)
				.exchange()
				.expectStatus().isOk(); //200
	}


	@Test
	public void deleteProductTest(){
         given(productService.deleteProduct(any())).willReturn(Mono.empty());

		webTestClient.delete().uri("/products/delete/102")
				.exchange()
				.expectStatus().isOk(); //200
	}

	@Test
	public void getProductInRangeTest(){
		Flux<ProductDto> productDtoFlux=Flux.just(new ProductDto("103","TV",2,2000),
				new ProductDto("104","watch",3,4000));
		when(productService.getProductInRange(1000,4000))
				.thenReturn(productDtoFlux);


		Flux<ProductDto> responseBody = webTestClient.get().uri("/product-range/2000,4000")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("103","TV",2,2000))
				.expectNext(new ProductDto("104","mobile.",1,4000))
				.verifyComplete();

	}


}
