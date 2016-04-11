package in.grasshoper.reports.data;

import in.grasshoper.core.exception.GeneralPlatformRuleException;

import java.util.List;
import java.util.Map;

public class ReportData {
	@SuppressWarnings("unused")private final List<String> header;
	@SuppressWarnings("unused")private final List<Map<String, Object>> dataList;
	
	private ReportData(List<String> header, List<Map<String, Object>> dataList) {
		super();
		this.header = header;
		this.dataList = dataList;
	}
	
	public static ReportData createNew(List<String> header, List<Map<String, Object>> dataList) {
		if(dataList != null && dataList.size() >0 && dataList.get(0) !=null){
			Map<String, Object> dataEntry =  dataList.get(0);
			if( dataEntry.keySet().size() != header.size())
				throw new GeneralPlatformRuleException("error.msg.data.list.count.not.matching.header", "Report data header count should match with data");
			return new ReportData(header, dataList);
		}
		
		return null;
	}
	
	
}
