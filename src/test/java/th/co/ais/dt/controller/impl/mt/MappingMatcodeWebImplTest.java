package th.co.ais.dt.controller.impl.mt;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import th.co.ais.dt.controller.dto.MappingMatcodeBean;
import th.co.ais.dt.core.controller.impl.mt.MappingMatcodeWebImpl;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IMappingMatcodeService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class MappingMatcodeWebImplTest {

    private MockMvc mvc;

    @InjectMocks
    private MappingMatcodeWebImpl mappingMatcodeWeb;

    @Mock
    private IMappingMatcodeService mappingMatcodeService;

    private JacksonTester<MappingMatcodeBean> mappingMatcodeBeanJacksonTester;

    @BeforeEach
    void setUp() {
        // use for validate json response
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(mappingMatcodeWeb).build();
    }

    @Test
    void queryMappingMatcode() throws Exception {
        // request
        String jsonReq = "{\"resultCode\":\"00\",\"resultDescription\":\"Success\",\"developerMessage\":\"None\"}";

        //mock value
        MappingMatcodeBean responseBean = new MappingMatcodeBean();
        responseBean.setResultCode("20000");
        responseBean.setResultDescription("Success");
        responseBean.setDeveloperMessage("Success");

        MappingMatcodeBean subResponseBean = new MappingMatcodeBean();
        subResponseBean.setResultCode("20000");
        subResponseBean.setResultDescription("Success");
        subResponseBean.setDeveloperMessage("Success");

        responseBean.setListMappingMatCode(List.of(subResponseBean));

        List<MappingMatcodeBean> responseBeanList = new ArrayList<>();
        responseBeanList.add(responseBean);

        ProcessResult processResult = new ProcessResult(true, responseBeanList , "Success");
        given(mappingMatcodeService.queryMappingMatcode(Mockito.any())).willReturn(processResult);

        MockHttpServletResponse response = mvc.perform(
                        post("/api/master-config/v1/query-mapping-matcode").content(jsonReq)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

//        assertThat(response.getContentAsString()).isEqualTo(
//                mappingMatcodeBeanJacksonTester.write(responseBean).getJson()
//        );

    }
}