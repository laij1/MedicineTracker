package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.clinic.anhe.medicinetracker.model.InventoryCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class InventoryViewModel extends ViewModel {

    private MutableLiveData<List<InventoryCardViewModel>> inventoryListLiveData;
    private List<InventoryCardViewModel> inventoryList;

    public InventoryViewModel() {
        inventoryListLiveData = new MutableLiveData<>();
        inventoryList = new ArrayList<>();
        inventoryListLiveData.setValue(inventoryList);
    }

    public MutableLiveData<List<InventoryCardViewModel>> getInventoryListLiveData() {
        return inventoryListLiveData;
    }
}
