package pinnacle.api.enums;

public enum PLACE_SPECIAL_BET_ERROR_CODE {

	ALL_BETTING_CLOSED,
	ABOVE_MAX_BET_AMOUNT,
	BELOW_MIN_BET_AMOUNT,
	BLOCKED_BETTING,
	BLOCKED_CLIENT,
	INSUFFICIENT_FUNDS,
	INVALID_COUNTRY,
	LINE_CHANGED,
	PAST_CUTOFFTIME,
	SYSTEM_ERROR_1,
	SYSTEM_ERROR_2,
	RESUBMIT_REQUEST,
	DUPLICATE_UNIQUE_REQUEST_ID,
	INVALID_REQUEST,
	UNIQUE_REQUEST_ID_REQUIRED,
	RESPONSIBLE_BETTING_RISK_LIMIT_EXCEEDED,
	RESPONSIBLE_BETTING_LOSS_LIMIT_EXCEEDED,
	INCOMPLETE_CUSTOMER_BETTING_PROFILE,
	WAGERING_SUSPENDED,
	CONTEST_NOT_FOUND,
	CONTEST_FUNCTIONALITY_IS_DISABLED,
	UNDEFINED;

	public String toAPI () {
		return this.name();
	}
	
	public static PLACE_SPECIAL_BET_ERROR_CODE fromAPI (String text) {
		try {
			return PLACE_SPECIAL_BET_ERROR_CODE.valueOf(text);
		} catch (IllegalArgumentException e) {
			return PLACE_SPECIAL_BET_ERROR_CODE.UNDEFINED;
		}
	}
}
