package in.grasshoper.core;

public interface GrassHoperMainConstants {
	boolean isDev = true;
	String LocaleParamName = "locale";
	String DefaultLocale = "en";
	String CurrencyCode = "QAR";
	String DefaultSiteName = "Grasshoper";
	String DefaultAppUrl = isDev ? "https://localhost:8443/grasshoper-core/" : "https://grasshoper-ghbluefin.rhcloud.com/grasshoper-core/";
}
