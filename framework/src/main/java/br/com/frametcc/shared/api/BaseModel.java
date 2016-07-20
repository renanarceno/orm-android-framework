package br.com.frametcc.shared.api;

import br.com.frametcc.database.dao.DatabaseDAO;

/**
 * Contrato para implementações da camada de modelo. Contém métodos para seu correto funcionamento
 *
 * @param <PRESENTER> presenter relacionado a esse modelo.
 */
public interface BaseModel<PRESENTER extends BasePresenter<?, ?>> {

	/**
	 * Chamado ao criar uma implementação de BaseMOdel
	 */
	void init();

	/**
	 * Seta o presenter no modelo
	 *
	 * @param presenter o presenter para ser setado
	 */
	void setPresenter(PRESENTER presenter);

	/**
	 * @param dao interface do DAO
	 * @return uma implementação de DAO para ser usada
	 */
	<T extends DatabaseDAO> T getDAO(Class<T> dao);

}