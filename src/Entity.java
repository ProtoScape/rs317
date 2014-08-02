/**
 * SceneModel [Animable]
 * Represents a single model on the Scene
 */
public class Entity extends CacheableNode {
	public void render(int arg0, int arg1, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9) {
		Model model = getRotatedModel();
		if (model != null) {
			height = model.height;
			model.render(arg0, arg1, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		}
	}

	Model getRotatedModel() {
		return null;
	}

	Entity() {
		height = 1000;
	}
	VertexNormal vertices[];
	public int height;
}
