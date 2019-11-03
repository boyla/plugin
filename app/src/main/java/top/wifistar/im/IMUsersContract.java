/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.wifistar.im;

import androidx.annotation.NonNull;


import top.wifistar.mvp.BasePresenter;
import top.wifistar.mvp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface IMUsersContract {

    interface View extends BaseView<Presenter> {
        void updateResultPage();

        void longClickItem(@NonNull IMUser item, int position);

        void clickItem(@NonNull IMUser item, int position);

    }

    interface Presenter extends BasePresenter {

        void reLogin();

//        RealmResults<IMUserRealm> loadIMUsers();

        void deleteIMUsers(Integer userId, String userName);

        void closeRealm();

    }
}