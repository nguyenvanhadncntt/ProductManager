package com.product.manager.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
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
import com.product.manager.entity.User;
import com.product.manager.exception.BadRequestException;
import com.product.manager.exception.NotFoundException;
import com.product.manager.repository.CategoryRepository;
import com.product.manager.repository.ProductRepository;
import com.product.manager.repository.UserRepository;
import com.product.manager.service.ProductService;
import com.product.manager.util.CSVUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

@Service
public class ProductServiceImpl implements ProductService {

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductCSVConvertToEntity productCSVConvertToEntity;
	
	@Autowired
	private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

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
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<ProductDTO> products = productRepository.searchProducts(pageable)
                .map(ProductDTO::fromEntity);
        return products;
    }

    @Override
    public ProductDTO getProduct(Long productId) throws NotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s does not exist", productId)));
        return ProductDTO.fromEntity(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, String email) throws NotFoundException {
        List<Product> products = productRepository.findProductByName(productDTO.getName());
        if (!products.isEmpty()) {
            throw new BadRequestException(String.format("Product with name %s is already exist", productDTO.getName()));
        }
        Product product = ProductDTO.toModel(productDTO);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            product.setCreatedBy(user);
        }

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
    public void updateProduct(Long productId, ProductDTO productDTO, String emailUser) throws NotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s does not exist", productId)));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());

        Optional<User> userOptional = userRepository.findByEmail(emailUser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            product.setUpdatedBy(user);
        }

        if (productDTO.getCategoryId() != null) {
            if (!categoryRepository.existsById(productDTO.getCategoryId())) {
                throw new NotFoundException(String.format("Category with id %s does not exist", productDTO.getCategoryId()));
            }
            Category category = categoryRepository.getOne(productDTO.getCategoryId());
            product.setCategory(category);
        }
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

    @Override
    public void exportProductToCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            StatefulBeanToCsv<ProductImportCSVDTO> writer =
                    new StatefulBeanToCsvBuilder<ProductImportCSVDTO>
                            (response.getWriter())
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .withOrderedResults(false).build();

            // write all employees to csv file
            writer.write(productCSVConvertToEntity.convertEntitiesToDtos(products));
        }
    }
}
