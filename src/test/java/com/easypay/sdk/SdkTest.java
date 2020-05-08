package com.easypay.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Map;

import org.junit.Test;

import com.easypay.sdk.service.OpenApiService;
import com.easypay.sdk.util.BeanUtils;
import com.easypay.sdk.util.DateUtils;
import com.easypay.sdk.util.IOUtils;
import com.easypay.sdk.vo.ApiDataResp;
import com.easypay.sdk.vo.ApiHeader;
import com.easypay.sdk.vo.CollectNotifyReqVO;
import com.easypay.sdk.vo.TokenReq;
import com.easypay.sdk.vo.TokenResp;
import com.easypay.sdk.vo.UploadFileReq;
import com.easypay.sdk.vo.UploadFileResp;

public class SdkTest {
	
	private static String sdkConfigPath = "";
	
	static {
		try {
			sdkConfigPath = URLDecoder.decode(SdkTest.class.getResource("/").getPath(), "UTF-8");
			// 如果是Windows, 去掉最前面的那个 /
	        if (System.getProperty("file.separator").equals("\\")) {
	        	sdkConfigPath = sdkConfigPath.substring(1) + "sdk.properties";
	        	System.out.println(sdkConfigPath);
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 获取token
	 * @throws Exception
	 * @author XY  
	 * @date 2020年4月22日
	 */
//    @Test
    public void getTokenTest() throws Exception {
        ApiServiceBean apiServiceBean = new ApiServiceBean();
        OpenApiService service = apiServiceBean.getService(sdkConfigPath);
        TokenReq req = new TokenReq();
        req.setTraceNo(String.valueOf(System.currentTimeMillis()));
        req.setSendTime(DateUtils.getApiSendTime());
		TokenResp token = service.getToken(req);
		System.out.println(token.toString());
    }
    
    /**
     * 
     * @Description: 收汇通知
     * @throws Exception
     * @author XY  
     * @date 2020年4月22日
     */
    @Test
    public void invokeTest() throws Exception {
    	ApiServiceBean apiServiceBean = new ApiServiceBean();
    	OpenApiService service = apiServiceBean.getService(sdkConfigPath);
    	String url = "rmb_collectNotify";
    	String traceNo = String.valueOf(System.currentTimeMillis());
    	String sendTime = DateUtils.getApiSendTime();
    	String token = "";

    	ApiHeader apiHeader = new ApiHeader();
    	apiHeader.setUrl(url);
    	apiHeader.setTraceNo(traceNo);
    	apiHeader.setSendTime(sendTime);
    	apiHeader.setToken(token);
    	
    	CollectNotifyReqVO reqVO = new CollectNotifyReqVO();
    	reqVO.setAcceptId("4816000001");
    	reqVO.setNotifyStatus("1"); // 通知状态(1-预入账通知 2-入账通知)
    	reqVO.setPayerName("beePay");
    	reqVO.setPayerAccount("451321648");
    	reqVO.setPayerBankId("BIST");
    	reqVO.setPayeeName("易生支付有限公司");
    	reqVO.setPayeeAccount("5810000010120100008643");
    	reqVO.setPayeeBankId("浙商银行");
    	reqVO.setRemitCyCode("01");
    	reqVO.setRemitAmount(BigDecimal.valueOf(150.00));
    	reqVO.setBusinessType("1");
    	reqVO.setRemitAddress("");
    	reqVO.setRemitRemark("rmborder|BR123456");
    	reqVO.setRemitDetail("汇款");
    	reqVO.setTransTime(DateUtils.getNowDate());
    	reqVO.setBusinessNo("CZ5580001");

    	Map<String, Object> reqData = BeanUtils.obj2Map(reqVO);

    	System.out.println(reqData);
    	
    	ApiDataResp apiDataResp = service.invoke(apiHeader, reqData);
    	System.out.println(apiDataResp.toString());
    }
    
    /**
     * 
     * @Description: 上传文件
     * @throws Exception
     * @author XY  
     * @date 2020年4月22日
     */
//    @Test
    public void uploadFileTest() throws Exception {
    	ApiServiceBean apiServiceBean = new ApiServiceBean();
    	OpenApiService service = apiServiceBean.getService(sdkConfigPath);
    	String traceNo = String.valueOf(System.currentTimeMillis());
    	String sendTime = DateUtils.getApiSendTime();
    			
    	String filePath = "D:\\CZTest";
		String fileName = "mytest.txt";
		String fileType = "txt";
    	
    	UploadFileReq reqVO = new UploadFileReq();
	    File file = new File(filePath + File.separator + fileName);
	    FileInputStream fis = new FileInputStream(file);

	    //设置上传文件二进制数据
	    reqVO.setFile(IOUtils.toByteArray(fis));
	    reqVO.setFileName(fileName);
	    reqVO.setFileType(fileType);
	    reqVO.setTraceNo(traceNo);
	    reqVO.setSendTime(sendTime);
	    
    	UploadFileResp uploadFile = service.uploadFile(reqVO);
    	System.out.println(uploadFile.toString());
    }

}

