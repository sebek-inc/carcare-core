package inc.sebec.carcare.core.configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.github.cloudyrock.mongock.Mongock;

import inc.sebec.carcare.core.config.MongockConfig;

@SpringBootTest
@MockBean(classes = {Mongock.class, MongockConfig.class})
public abstract class BaseTest {}
