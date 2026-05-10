package th.co.ais.dt.core.service.core.impl.sap;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.ais.dt.controller.dto.DtSapMchMapping;
import th.co.ais.dt.controller.dto.MCHDescriptions;
import th.co.ais.dt.controller.dto.MchItem;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;
import th.co.ais.dt.entity.sap.DtSapMatGroupMapProduct;
import th.co.ais.dt.entity.sap.DtSapMatGroupMapType;
import th.co.ais.dt.entity.sap.DtSapMchProductPriceLog;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMatGroupMapProductDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMatGroupMapTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMchProductPriceLogDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.mail.impl.EmailSenderService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SapHandleMchMappingServiceImplTest {

    @InjectMocks
    private SapHandleMchMappingServiceImpl sapHandleMchMappingService;

    @Mock
    private ILovMasterDao lovMasterDao;

    @Mock
    private IEmailService emailService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private IDtSapMatGroupMapTypeDao dtSapMatGroupMapTypeDao;

    @Mock
    private IDtSapMatGroupMapProductDao dtSapMatGroupMapProductDao;

    @Mock
    private IDtSapMchProductPriceLogDao dtSapMchProductPriceLogDao;

    private DtSapMchMapping testRequest;
    private LovMaster testConfig;
    private DtSapMchProductPriceLog testLog;

    @BeforeEach
    void setUp() {
        // Setup test configuration
        testConfig = new LovMaster();
        testConfig.setLovVal("10|90");

        // Setup test log
        testLog = new DtSapMchProductPriceLog();
        testLog.setTableId(1L);
        testLog.setServiceName("MCH_MAPPING_PRODUCT");
        testLog.setStatus("W");

        // Setup test request
        testRequest = new DtSapMchMapping();
        testRequest.setITEMS(createTestMchItems());
    }

    @Test
    void testMappingMatGroupProductSuccess() throws MessagingException {
        // Arrange
        when(lovMasterDao.listLovMasterByCriteria(
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                null, null, "Y"))
                .thenReturn(List.of(testConfig));

        doAnswer(invocation -> {
            DtSapMchProductPriceLog log = invocation.getArgument(0);
            log.setTableId(1L);
            return null;
        }).when(dtSapMchProductPriceLogDao).insert(any(DtSapMchProductPriceLog.class));

        when(dtSapMatGroupMapProductDao.getSapMatGroupMapProductById("901001000"))
                .thenReturn(null);

        when(dtSapMatGroupMapTypeDao.listSapMatGroupMapTypeForMappingType("90100", "901001000"))
                .thenReturn(createTestMapTypeList());

        when(dtSapMchProductPriceLogDao.getByLongPrimaryKey(any(Long.class)))
                .thenReturn(testLog);

        doNothing().when(dtSapMchProductPriceLogDao).update(any(DtSapMchProductPriceLog.class));
        doNothing().when(dtSapMatGroupMapProductDao).insertList(anyList());
        doNothing().when(emailSenderService).sendEmail_html(any(EmailForm.class));
        when(emailService.queryEmail("MCH_MAPPING_PRODUCT"))
                .thenReturn(createTestEmailQueryResult());

        // Act
        sapHandleMchMappingService.mappingMatGroupProduct(testRequest);

        // Assert
        verify(dtSapMatGroupMapProductDao).insertList(any(List.class));
        verify(dtSapMchProductPriceLogDao).update(any(DtSapMchProductPriceLog.class));
        verify(emailSenderService).sendEmail_html(any(EmailForm.class));

        ArgumentCaptor<DtSapMchProductPriceLog> logCaptor = ArgumentCaptor.forClass(DtSapMchProductPriceLog.class);
        verify(dtSapMchProductPriceLogDao).update(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("S");
    }

    @Test
    void testMappingMatGroupProductWithEmptyFilteredItems() {
        // Arrange - config doesn't match any items
        LovMaster emptyConfig = new LovMaster();
        emptyConfig.setLovVal("80");

        when(lovMasterDao.listLovMasterByCriteria(
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                null, null, "Y"))
                .thenReturn(List.of(emptyConfig));

        // Act
        sapHandleMchMappingService.mappingMatGroupProduct(testRequest);

        // Assert - should return early without processing
        verify(dtSapMchProductPriceLogDao, org.mockito.Mockito.never()).insert(any());
    }

    @Test
    void testMappingMatGroupProductWithAlreadyExistingItem() throws MessagingException {
        // Arrange
        when(lovMasterDao.listLovMasterByCriteria(
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                null, null, "Y"))
                .thenReturn(List.of(testConfig));

        doAnswer(invocation -> {
            DtSapMchProductPriceLog log = invocation.getArgument(0);
            log.setTableId(1L);
            return null;
        }).when(dtSapMchProductPriceLogDao).insert(any(DtSapMchProductPriceLog.class));

        // Material group already exists in database
        DtSapMatGroupMapProduct existingProduct = new DtSapMatGroupMapProduct();
        existingProduct.setMaterialGroup("901001000");
        when(dtSapMatGroupMapProductDao.getSapMatGroupMapProductById("901001000"))
                .thenReturn(existingProduct);

        when(dtSapMchProductPriceLogDao.getByLongPrimaryKey(any(Long.class)))
                .thenReturn(testLog);

        doNothing().when(dtSapMchProductPriceLogDao).update(any(DtSapMchProductPriceLog.class));
        doNothing().when(emailSenderService).sendEmail_html(any(EmailForm.class));
        when(emailService.queryEmail("MCH_MAPPING_PRODUCT"))
                .thenReturn(createTestEmailQueryResult());

        // Act
        sapHandleMchMappingService.mappingMatGroupProduct(testRequest);

        // Assert
        verify(dtSapMatGroupMapProductDao, org.mockito.Mockito.never()).insertList(any());

        ArgumentCaptor<DtSapMchProductPriceLog> logCaptor = ArgumentCaptor.forClass(DtSapMchProductPriceLog.class);
        verify(dtSapMchProductPriceLogDao).update(logCaptor.capture());
        assertThat(logCaptor.getValue().getDescription()).contains("Already exists");
    }

    @Test
    void testMappingMatGroupProductWithoutProductTypeMapping() throws MessagingException {
        // Arrange
        when(lovMasterDao.listLovMasterByCriteria(
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                null, null, "Y"))
                .thenReturn(List.of(testConfig));

        doAnswer(invocation -> {
            DtSapMchProductPriceLog log = invocation.getArgument(0);
            log.setTableId(1L);
            return null;
        }).when(dtSapMchProductPriceLogDao).insert(any(DtSapMchProductPriceLog.class));

        when(dtSapMatGroupMapProductDao.getSapMatGroupMapProductById("901001000"))
                .thenReturn(null);

        // No mapping type found, returns empty list
        when(dtSapMatGroupMapTypeDao.listSapMatGroupMapTypeForMappingType("90100", "901001000"))
                .thenReturn(new ArrayList<>());

        when(dtSapMchProductPriceLogDao.getByLongPrimaryKey(any(Long.class)))
                .thenReturn(testLog);

        doNothing().when(dtSapMchProductPriceLogDao).update(any(DtSapMchProductPriceLog.class));
        doNothing().when(dtSapMatGroupMapProductDao).insertList(anyList());
        doNothing().when(emailSenderService).sendEmail_html(any(EmailForm.class));
        when(emailService.queryEmail("MCH_MAPPING_PRODUCT"))
                .thenReturn(createTestEmailQueryResult());

        // Act
        sapHandleMchMappingService.mappingMatGroupProduct(testRequest);

        // Assert
        ArgumentCaptor<DtSapMchProductPriceLog> logCaptor = ArgumentCaptor.forClass(DtSapMchProductPriceLog.class);
        verify(dtSapMchProductPriceLogDao).update(logCaptor.capture());
        assertThat(logCaptor.getValue().getDescription()).contains("Product type subtype not config");
    }

    @Test
    void testMappingMatGroupProductExceptionHandling() throws MessagingException {
        // Arrange
        when(lovMasterDao.listLovMasterByCriteria(
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                SapHandleMchMappingServiceImpl.SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                null, null, "Y"))
                .thenReturn(List.of(testConfig));

        doAnswer(invocation -> {
            DtSapMchProductPriceLog log = invocation.getArgument(0);
            log.setTableId(1L);
            return null;
        }).when(dtSapMchProductPriceLogDao).insert(any(DtSapMchProductPriceLog.class));

        when(dtSapMatGroupMapProductDao.getSapMatGroupMapProductById("901001000"))
                .thenThrow(new RuntimeException("getSapMatGroupMapProductById error"));

        when(dtSapMchProductPriceLogDao.getByLongPrimaryKey(any(Long.class)))
                .thenReturn(testLog);

        doNothing().when(dtSapMchProductPriceLogDao).update(any(DtSapMchProductPriceLog.class));
        doNothing().when(emailSenderService).sendEmail_html(any(EmailForm.class));
        when(emailService.queryEmail("MCH_MAPPING_PRODUCT"))
                .thenReturn(createTestEmailQueryResult());

        // Act & Assert
        sapHandleMchMappingService.mappingMatGroupProduct(testRequest);

        ArgumentCaptor<DtSapMchProductPriceLog> logCaptor = ArgumentCaptor.forClass(DtSapMchProductPriceLog.class);
        verify(dtSapMchProductPriceLogDao).update(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("F");
        assertThat(logCaptor.getValue().getDescription()).contains("getSapMatGroupMapProductById error");

        verify(emailSenderService).sendEmail_html(any(EmailForm.class));
    }

    // Helper methods
    private List<MchItem> createTestMchItems() {
        List<MchItem> items = new ArrayList<>();

        MchItem item1 = new MchItem();
        item1.setLevelofHierarchy("00");
        item1.setMaterialGroupCode("901001000");

        MCHDescriptions description1 = new MCHDescriptions();
        description1.setMCMCHDescription("Test Material Group 1");
        description1.setMCDescription2("Test Material Group Description 2");
        item1.setMCHDescriptions(description1);

        items.add(item1);

        /*MchItem item2 = new MchItem();
        item2.setLevelofHierarchy("00");
        item2.setMaterialGroupCode("902001000");

        items.add(item2);*/

        // Non-matching hierarchy level
        MchItem item3 = new MchItem();
        item3.setLevelofHierarchy("01");
        item3.setMaterialGroupCode("990001");

        items.add(item3);

        return items;
    }

    private List<DtSapMatGroupMapType> createTestMapTypeList() {
        List<DtSapMatGroupMapType> mapTypes = new ArrayList<>();

        DtSapMatGroupMapType mapType = new DtSapMatGroupMapType();
        mapType.setMaterialGroupLv3("901001000");
        mapType.setProductType("PROD_TYPE_1");
        mapType.setProductSubtype("PROD_SUBTYPE_1");

        mapTypes.add(mapType);

        return mapTypes;
    }

    private List<Object[]> createTestEmailQueryResult() {
        List<Object[]> result = new ArrayList<>();
        Object[] emailRow = new Object[]{
                1L,
                "MCH_MAPPING_PRODUCT",
                "EMAIL_TYPE",
                "MCH Mapping Product Status",
                "test@example.com",
                "cc@example.com"
        };
        result.add(emailRow);
        return result;
    }
}
