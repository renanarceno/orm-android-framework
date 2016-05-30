package br.com.frametcc.shared.api;

import br.com.frametcc.database.dao.DatabaseDAO;

public interface BaseModel<PRESENTER extends BasePresenter<?, ?>> {

    void init();

    void setPresenter(PRESENTER presenter);

    <T extends DatabaseDAO> T getDAO(Class<T> dao);

}