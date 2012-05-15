public class Category extends Feature {

	enum Categories {
		Entertainment,
		Automotive,
		FoodAndDining,
		HealthAndBeauty,
		Retail,
		SportsAndRecreation,
		Travel,
		ClothingAndApparel,
		ElectronicsAndAppliances,
		FurnitureAndDecor,
		Grocery,
		HobbiesAndCrafts,
		HomeServices,
		HotelsAndLodging,
		NightlifeAndBars,
		NonProfitsAndYouth,
		OfficeSupplies,
		Other,
		PetServices,
		ProfessionalServices,
		RealEstate,
		Unknown
	}
	
	private Categories category;

	public Category() {
		super("Category");
	}
	
	public Category(Categories category) {
		super("Category");
		this.category = category;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}

	public Categories getCategory() {
		return category;
	}
	
	public static Categories determineCategory(String category) {
		if(Integer.parseInt(category) == 0) {
			return Categories.Entertainment;
		}
		else if(Integer.parseInt(category) == 1) {
			return Categories.Automotive;
		}
		else if(Integer.parseInt(category) == 2) {
			return Categories.FoodAndDining;
		}
		else if(Integer.parseInt(category) == 3) {
			return Categories.HealthAndBeauty;
		}
		else if(Integer.parseInt(category) == 4) {
			return Categories.Retail;
		}
		else if(Integer.parseInt(category) == 5) {
			return Categories.SportsAndRecreation;
		}
		else if(Integer.parseInt(category) == 6) {
			return Categories.Travel;
		}
		else if(Integer.parseInt(category) == 7) {
			return Categories.ClothingAndApparel;
		}
		else if(Integer.parseInt(category) == 8) {
			return Categories.ElectronicsAndAppliances;
		}
		else if(Integer.parseInt(category) == 9) {
			return Categories.FurnitureAndDecor;
		}
		else if(Integer.parseInt(category) == 10) {
			return Categories.Grocery;
		}
		else if(Integer.parseInt(category) == 11) {
			return Categories.HobbiesAndCrafts;
		}
		else if(Integer.parseInt(category) == 12) {
			return Categories.HomeServices;
		}
		else if(Integer.parseInt(category) == 13) {
			return Categories.HotelsAndLodging;
		}
		else if(Integer.parseInt(category) == 14) {
			return Categories.NightlifeAndBars;
		}
		else if(Integer.parseInt(category) == 15) {
			return Categories.NonProfitsAndYouth;
		}
		else if(Integer.parseInt(category) == 16) {
			return Categories.OfficeSupplies;
		}
		else if(Integer.parseInt(category) == 17) {
			return Categories.Other;
		}
		else if(Integer.parseInt(category) == 18) {
			return Categories.PetServices;
		}
		else if(Integer.parseInt(category) == 19) {
			return Categories.ProfessionalServices;
		}
		else if(Integer.parseInt(category) == 20) {
			return Categories.RealEstate;
		}
		else if(Integer.parseInt(category) == 21) {
			return Categories.Unknown;
		}
		else {
			return Categories.Unknown;
		}
	}

}
