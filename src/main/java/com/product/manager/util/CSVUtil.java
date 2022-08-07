package com.product.manager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.product.manager.dto.ProductImportCSVDTO;

public class CSVUtil {
	Logger logger = LoggerFactory.getLogger(CSVUtil.class);
	
	public static String TYPE = "text/csv";

	public static boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<ProductImportCSVDTO> convertCsvFileToProductObject(InputStream is) throws IOException {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
				CSVReader csvReader = new CSVReader(fileReader)) {
			HeaderColumnNameMappingStrategy<ProductImportCSVDTO> strategy = new HeaderColumnNameMappingStrategy<>();
			strategy.setType(ProductImportCSVDTO.class);

			CsvToBean<ProductImportCSVDTO> csvToBean = new CsvToBeanBuilder<ProductImportCSVDTO>(csvReader)
					.withMappingStrategy(strategy)
					.withIgnoreLeadingWhiteSpace(true).build();

			return csvToBean.parse();
		}
	}
}
