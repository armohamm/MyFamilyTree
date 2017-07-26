package com.cxb.myfamilytree.presenter;

import com.cxb.myfamilytree.config.Config;
import com.cxb.myfamilytree.model.FamilyBean;
import com.cxb.myfamilytree.model.FamilyModel;
import com.cxb.myfamilytree.model.IFamilyModel;
import com.cxb.myfamilytree.view.ILaunchView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.cxb.myfamilytree.model.FamilyBean.SEX_MALE;

/**
 * 启动页Presenter实现
 */

public class LaunchPresenter implements ILaunchPresenter {

    private ILaunchView mView;
    private IFamilyModel mModel;

    public LaunchPresenter() {
        mModel = new FamilyModel();
    }

    @Override
    public void getFamily(String familyId) {
        mModel.findFamilyById(familyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<FamilyBean>() {
                            @Override
                            public void accept(@NonNull FamilyBean family) throws Exception {
                                if (isActive()) {
                                    mView.startMainActivity();
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (isActive()) {
                                    addFamily();
                                }
                            }
                        });
    }

    private void addFamily() {
        FamilyBean family = new FamilyBean();
        family.setMemberId(Config.MY_ID);
        family.setMemberName("我的姓名");
        family.setCall("我");
        family.setSex(SEX_MALE);
        family.setBirthday("");

        mModel.saveFamily(family)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (isActive()) {
                            mView.startMainActivity();
                        }
                    }
                })
                .subscribe();
    }

    private boolean isActive() {
        return mView != null;
    }

    @Override
    public void attachView(ILaunchView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
