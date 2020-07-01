package inc.sebec.carcare.core.controller;

public interface API {
	String API = "/api";

	interface V1 {
		String V1 = "/v1";
		String REPAIR_SHOP = API + V1 + "/repair-shops";
		String AUTH = API + V1 + "/auth";
		String PROFILES = API + V1 + "/profiles";
	}
}
