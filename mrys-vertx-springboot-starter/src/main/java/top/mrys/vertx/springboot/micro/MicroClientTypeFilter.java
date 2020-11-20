package top.mrys.vertx.springboot.micro;

import java.io.IOException;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/**
 * @author mrys
 * @date 2020/11/20
 */
public class MicroClientTypeFilter implements TypeFilter {

  @Override
  public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
      throws IOException {
    return false;
  }
}
