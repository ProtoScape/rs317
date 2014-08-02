final class GroundItem extends Entity {
	@Override
	public final Model getRotatedModel() {
		ItemDefinition item = ItemDefinition.forId(id);
		return item.preProcess(amount);
	}

	public GroundItem() {
	}
	public int id;
	public int amount;
}
