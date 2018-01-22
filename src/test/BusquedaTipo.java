package test;

public enum BusquedaTipo {
	BODEGA, CASA, TERRENO, OFICINA;

	private BusquedaTipo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Convierte el string dado en enum o regresa null si no se pudo convertir
	 * 
	 * 
	 * @param tipo
	 * @return
	 */
	public static BusquedaTipo getValueOf(String tipo) {
		BusquedaTipo transaccion = null;

		if (tipo == null || tipo.isEmpty()) {
			return null;
		}

		tipo = tipo.toLowerCase().trim();
		if (tipo.contains("bodega")) {
			transaccion = BODEGA;
		} else if (tipo.contains("casa")) {
			transaccion = CASA;
		} else if (tipo.contains("oficina")) {
			transaccion = OFICINA;
		} else if (tipo.contains("terreno")) {
			transaccion = TERRENO;
		}

		return transaccion;
	}

}
