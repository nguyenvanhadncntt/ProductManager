package com.product.manager.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.product.manager.convert.impl.ProductCSVConvertToEntity;
import com.product.manager.dto.ProductDTO;
import com.product.manager.dto.ProductImportCSVDTO;
import com.product.manager.entity.Category;
import com.product.manager.entity.Product;
import com.product.manager.exception.BadRequestException;
import com.product.manager.exception.NotFoundException;
import com.product.manager.repository.CategoryRepository;
import com.product.manager.repository.ProductRepository;
import com.product.manager.service.ProductService;
import com.product.manager.util.CSVUtil;

@Service
public class ProductServiceImpl implements ProductService {

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductCSVConvertToEntity productCSVConvertToEntity;
	
	@Autowired
	private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

	@Override
	@Transactional
	public void importProductFromCSVFile(MultipartFile file) {
		try {
			List<ProductImportCSVDTO> productFromCSVs = CSVUtil.convertCsvFileToProductObject(file.getInputStream());
			List<Product> products = productCSVConvertToEntity.convertToProducts(productFromCSVs);
			productRepository.saveAll(products);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Cannot read csv file {}", file.getOriginalFilename());
		}
	}

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
        return products;
    }

    @Override
    public ProductDTO getProduct(Long productId) throws NotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s does not exist", productId)));
        return ProductDTO.fromEntity(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws NotFoundException {
        List<Product> products = productRepository.findProductByName(productDTO.getName());
        if (!products.isEmpty()) {
            throw new BadRequestException(String.format("Product with name %s is already exist", productDTO.getName()));
        }
        Product product = ProductDTO.toModel(productDTO);
        if (productDTO.getCategoryId() != null) {
            if (!categoryRepository.existsById(productDTO.getCategoryId())) {
                throw new NotFoundException(String.format("Category with id %s does not exist", productDTO.getCategoryId()));
            }
            Category category = categoryRepository.getOne(productDTO.getCategoryId());
            product.setCategory(category);
        }
        productRepository.save(product);
        return ProductDTO.fromEntity(product);
    }

    @Override
    public void updateProduct(Long productId, ProductDTO productDTO) throws NotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s does not exist", productId)));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        if (productDTO.getCategoryId() != null) {
            if (!categoryRepository.existsById(productDTO.getCategoryId())) {
                throw new NotFoundException(String.format("Category with id %s does not exist", productDTO.getCategoryId()));
            }
            Category category = categoryRepository.getOne(productDTO.getCategoryId());
            product.setCategory(category);
        }
//        product.setUpdatedBy(productDTO.getUpdatedBy());
        product.setUpdatedDate(Instant.now());

        productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteProduct(Long productId) throws NotFoundException {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(String.format("Product with id %s does not exist", productId));
        }
        productRepository.deleteById(productId);
    }
}
