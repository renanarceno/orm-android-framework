package br.com.frametcc.shared;

import br.com.frametcc.TCCApplication;
import br.com.frametcc.database.DBConnectionHelper;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.shared.api.BaseModel;
import br.com.frametcc.shared.api.BasePresenter;

public abstract class AbstractBaseModel<PRESENTER extends BasePresenter<?, ?>> implements BaseModel<PRESENTER> {

    protected PRESENTER presenter;

    @Override
    public void init() {

    }

    public void setPresenter(PRESENTER presenter) {
        this.presenter = presenter;
    }

    @Override
    public final PRESENTER getPresenter() {
        return null;
    }

    @Override
    public final <T extends DatabaseDAO> T getDAO(Class<T> dao) {
        DBConnectionHelper connection = TCCApplication.getDbConnection();
        if (connection == null)
            return null;
        return connection.getDao(dao);
    }
}