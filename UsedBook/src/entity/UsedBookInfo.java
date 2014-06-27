package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.drawable.Drawable;

public class UsedBookInfo implements Serializable{

	String infoId=null;
	String userId=null;
	boolean isOffer=false;
	boolean isFree=false;
	boolean isWant=false;
	boolean isEffective=true;
	String title=null;
	String content=null;
	Date createDate= null;
    
	boolean hasPhoto=false;
	
	ArrayList<String> originalPhotoURL = null;
	ArrayList<String> zippedPhotoURL = null;


	public boolean isWant() {
		return isWant;
	}
	public void setWant(boolean isWant) {
		this.isWant = isWant;
	}	

    public UsedBookInfo(){
    }

	public boolean isHasPhoto() {
		return hasPhoto;
	}
	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}
	public boolean isEffective() {
		return isEffective;
	}
	public void setEffective(boolean isEffective) {
		this.isEffective = isEffective;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isOffer() {
		return isOffer;
	}
	public void setOffer(boolean isOffer) {
		this.isOffer = isOffer;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		return "UsedBookInfo [infoId=" + infoId + ", userId=" + userId
				+ ", isOffer=" + isOffer + ", isFree=" + isFree + ", isWant="
				+ isWant + ", isEffective=" + isEffective + ", title=" + title
				+ ", content=" + content + ", createDate=" + createDate
				+ ", hasPhoto=" + hasPhoto + ", originalPhotoURL="
				+ originalPhotoURL + ", zippedPhotoURL=" + zippedPhotoURL + "]";
	}
	public ArrayList<String> getOriginalPhotoURL() {
		return originalPhotoURL;
	}
	public void setOriginalPhotoURL(ArrayList<String> originalPhotoURL) {
		this.originalPhotoURL = originalPhotoURL;
	}
	public ArrayList<String> getZippedPhotoURL() {
		return zippedPhotoURL;
	}
	public void setZippedPhotoURL(ArrayList<String> zippedPhotoURL) {
		this.zippedPhotoURL = zippedPhotoURL;
	}

}