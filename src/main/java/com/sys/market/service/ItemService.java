package com.sys.market.service;

import com.sys.market.advice.exception.CBadRequestException;
import com.sys.market.advice.exception.CNotFoundException;
import com.sys.market.dto.criteria.ItemSearchCriteria;
import com.sys.market.entity.Item;
import com.sys.market.entity.ItemImage;
import com.sys.market.mapper.ItemImageMapper;
import com.sys.market.mapper.ItemMapper;
import com.sys.market.mapper.OfferMapper;
import com.sys.market.util.file.ImageFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ItemService {
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    OfferMapper offerMapper;
    @Autowired
    ItemImageMapper itemImageMapper;
    @Autowired
    ImageFileUtil fileUtil;

    @Transactional
    public Item findItem(Integer id) {
        Item item = itemMapper.findItemById(id);
        if(item != null){
            item.setImages(itemImageMapper.selectItemImagePathList(id));
        }
        return item;
    }

    @Transactional
    public List<Item> findItemList(ItemSearchCriteria criteria) {
        List<Item> items = itemMapper.selectItemList(criteria);

        for(Item item : items){
            item.setThumbImage(fileUtil.nameToThumbName(item.getThumbImage()));
        }
        return items;
    }

    public int findItemCount(ItemSearchCriteria criteria) {
        return itemMapper.selectItemCount(criteria);
    }

    public List<Integer> findItemImageIdList(Integer id) {
        return itemImageMapper.selectItemImageIdList(id);
    }

    public Integer addItem(Item itemInfo) {
        itemMapper.insertItem(itemInfo);
        return itemInfo.getId();
    }

    public void modifyItem(Item item) {
        itemMapper.updateItem(item);
    }

    @Transactional
    public void modifyItemImage(Integer id, MultipartFile[] files, Integer[] imageIds){
        int length = getImageFileLength(files, imageIds);

        // ?????? ???????????? ????????? ?????????
        List<String> oldPaths = itemImageMapper.selectItemImagePathList(id);
        List<Integer> oldIds = itemImageMapper.selectItemImageIdList(id);

        HashMap<Integer, String> oldImages = new HashMap<>();
        for(int i = 0; i < oldIds.size(); i++){
            oldImages.put(oldIds.get(i), oldPaths.get(i));
        }

        if(imageIds != null){
            List<Integer> ids = Arrays.asList(imageIds);

            if(!imagesOwnedByItem(ids, oldIds)){
                throw new CBadRequestException("?????? ????????? ?????? ????????? ??????");
            }

            oldIds.removeAll(ids); // (?????? ????????? - ?????????) ????????? ??????????????? ?????????
        }

        List<ItemImage> updateItemImageList = new ArrayList<>();
        List<ItemImage> insertItemImageList = new ArrayList<>();

        for(int i = 0; i < length; i++){
            ItemImage itemImage = new ItemImage();
            itemImage.setItemId(id);
            itemImage.setIdx(i);

            if(files[i].getSize() > 0 && imageIds[i] == -1){ // ????????? ????????? ?????????
                String path;
                path = fileUtil.uploadImage(files[i]);

                itemImage.setPath(path);
                insertItemImageList.add(itemImage);
            }else if(files[i].getSize() <= 0 && imageIds[i] != -1){ // ?????? ????????? ??????
                itemImage.setId(imageIds[i]);
                updateItemImageList.add(itemImage);
            }
        }

        if(!insertItemImageList.isEmpty()) {
            itemImageMapper.insertItemImages(insertItemImageList); // ????????? ????????? ??????
        }
        if(!updateItemImageList.isEmpty()){
            itemImageMapper.updateItemImages(updateItemImageList);
        }
        if(!oldIds.isEmpty()){
            itemImageMapper.deleteItemImages(oldIds);
            for (Integer oldId : oldIds) {
                fileUtil.deleteImage(oldImages.get(oldId));
                fileUtil.deleteImage(fileUtil.nameToThumbName(oldImages.get(oldId)));
            }
        }

        // item??? thumbImageId??? ????????????
        itemMapper.updateItemThumbImageId(id);
    }

    @Transactional
    public void removeItem(Integer id) {
        itemMapper.deleteItem(id);
        itemImageMapper.deleteItemImageByItemId(id);
        offerMapper.updateOfferComplete(id);
    }

    // ??????, ?????? ?????? ??????. ????????? ?????? ??????????????? true
    @Transactional
    public boolean ownedByUser(String userId, Integer id){
        Item item = itemMapper.findItemById(id);
        if(item == null) throw new CNotFoundException();

        return item.getUserId().equals(userId);
    }

    private int getImageFileLength(MultipartFile[] files, Integer[] imageIds){
        if(files!=null && imageIds!=null){
            if(files.length != imageIds.length)
                throw new CBadRequestException();

            return files.length;
        }
        return 0;
    }

    // ????????? ?????? ???????????? ??????????????? ????????? ??????
    private boolean imagesOwnedByItem(List<Integer> ids, List<Integer> oldIds){
        List<Integer> tempNewIds = new ArrayList<>(ids);
        tempNewIds.removeAll(oldIds);
        tempNewIds.removeAll(Collections.singletonList(-1)); // id?????? -1??? ?????? ?????? ???????????? ??????????????? ????????? ?????????.

        // (????????? - ?????? ?????????) ???????????? ???????????? ????????????, ?????? ???????????? ???????????? ?????? ???????????? ??????????????? ???
        return tempNewIds.isEmpty();
    }
}
