package com.bob.android.mockactionlocation.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bob.android.mockactionlocation.entity.UserEntity;

import java.util.List;

/**
 * @package com.bob.android.mockactionlocation.model
 * @fileName UserModel
 * @Author Bob on 2019/1/11 17:09.
 * @Describe TODO
 */
public class UserModel extends ViewModel {

    private MutableLiveData<List<UserEntity>> userList;

    public LiveData<List<UserEntity>> getUsers(){
        if(null == userList){
            userList = new MutableLiveData<>();
            loadUsers();
        }
        return userList;
    }

    private void loadUsers() {

    }
}
