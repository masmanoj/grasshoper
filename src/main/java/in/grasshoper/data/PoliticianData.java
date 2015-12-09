package in.grasshoper.data;

import java.io.Serializable;

public class PoliticianData implements Serializable {

    private final Long id;
    private final String name;

    public static PoliticianData instance(final Long id, final String name) {
        return new PoliticianData(id, name);
    }

    private PoliticianData(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
}
