package models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Site implements Serializable{

	private static final int NO_IMAGE = -1;
	private static final long serialVersionUID = 1L;
	private String idSite;
	private String name;
	private String urlPhoto;
	private String positionX;
	private String positionY;
	private String info;
	private String modifiedDate;
	private String nameOwner;
	private String ownerId;
	transient Bitmap bitmap;
	private String idMarker;

	
	public Site(){
		super();
	}
	
	public Site(String idSite, String name, String urlPhoto, String positionX,
			String positionY, String info, String nameOwner, String ownerId) {
		super();
		this.idSite = idSite;
		this.name = name;
		this.urlPhoto = urlPhoto;
		this.positionX = positionX;
		this.positionY = positionY;
		this.info = info;
		this.modifiedDate = this.obtainTimestamp();
		this.nameOwner= nameOwner;
		this.ownerId = ownerId;
	}
	
	public Site(String idSite, String name, String urlPhoto, String positionX,
			String positionY, String nameOwner, String ownerId) {
		super();
		this.idSite = idSite;
		this.name = name;
		this.urlPhoto = urlPhoto;
		this.positionX = positionX;
		this.positionY = positionY;
		this.nameOwner= nameOwner;
		this.ownerId = ownerId;
	}

	public String obtainTimestamp() {		
		Date utilDate = new java.util.Date();
		Timestamp timestamp = new Timestamp(utilDate.getTime());
		String time = timestamp.toString();
		time = time.substring(0, time.indexOf("."));
		return time;
	}	
	public String getIdSite() {
		return idSite;
	}
	public void setIdSite(String idSite) {
		this.idSite = idSite;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlPhoto() {
		return urlPhoto;
	}
	public void setUrlPhoto(String urlPhoto) {
		this.urlPhoto = urlPhoto;
	}
	public String getPositionX() {
		return positionX;
	}
	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}
	public String getPositionY() {
		return positionY;
	}
	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getNameOwner() {
		return nameOwner;
	}

	public void setNameOwner(String nameOwner) {
		this.nameOwner = nameOwner;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
	       // This will serialize all fields that you did not mark with 'transient'
	       // (Java's default behaviour)
	        oos.defaultWriteObject();
	       // Now, manually serialize all transient fields that you want to be serialized
	        if(bitmap!=null){
	            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
	            if(success){
	                oos.writeObject(byteStream.toByteArray());
	            }
	        }
	    }

	    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
	       // Now, all again, deserializing - in the SAME ORDER!
	       // All non-transient fields
	        ois.defaultReadObject();
	       // All other fields that you serialized
	        byte[] image = (byte[]) ois.readObject();
	        if(image != null && image.length > 0){
	            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
	        }
	    }
	
//	private void writeObject(ObjectOutputStream out) throws IOException {
//	    if (bitmap != null) {
//	        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//	        final byte[] imageByteArray = stream.toByteArray();
//	        out.writeInt(imageByteArray.length);
//	        out.write(imageByteArray);
//	    } else {
//	        out.writeInt(NO_IMAGE);
//	    }
//	}
//
//	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//
//	    final int length = in.readInt();
//
//	    if (length != NO_IMAGE) {
//	        final byte[] imageByteArray = new byte[length];
//	        bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, length);
//	    }
//	}

	public String getIdMarker() {
		return idMarker;
	}

	public void setIdMarker(String idMarker) {
		this.idMarker = idMarker;
	}
}
