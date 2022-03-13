package vo;

public class GS_VO {

	private String addr;
	private String code;
	private String inventory;
	private String name;
	private String price;
	private String tel;
	private String regDt;

	public GS_VO() {
	}

	public GS_VO(String addr, String code, String inventory, String name, String price, String tel, String regDt) {
		super();
		this.addr = addr;
		this.code = code;
		this.inventory = inventory;
		this.name = name;
		this.price = price;
		this.tel = tel;
		this.regDt = regDt;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

}