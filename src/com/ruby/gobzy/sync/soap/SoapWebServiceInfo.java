package com.ruby.gobzy.sync.soap;

public class SoapWebServiceInfo {
	
	//public static final String RESULT = "Result";
	
	public static final String URL = "http://rkandro.com/WebService.asmx";

    public static final String CATEGORY_LIST_ENVELOPE = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><GetCategoryList xmlns=\"http://tempuri.org/\" /></soap:Body></soap:Envelope>";
    public static final String CATEGORY_LIST_SOAP_ACTION = "http://tempuri.org/GetCategoryList";
    public static final String CATEGORY_LIST_RESULT_TAG = "GetCategoryListResult";

    public static final String STORY_ENVELOPE = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><GetListByCategoryId xmlns=\"http://tempuri.org/\"><Category_Id>%s</Category_Id></GetListByCategoryId></soap:Body></soap:Envelope>";
    public static final String STORY_SOAP_ACTION = "http://tempuri.org/GetListByCategoryId";
    public static final String STORY_RESULT_TAG = "GetListByCategoryIdResult";

    public static final String CHECK_UPDATE_ENVELOPE = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><GetStoryCount xmlns=\"http://tempuri.org/\" /></soap:Body></soap:Envelope>";
    public static final String CHECK_UPDATE_ACTION = "http://tempuri.org/GetStoryCount";
    public static final String CHECK_UPDATE_RESULT_TAG = "GetStoryCountResult";
}
