package model;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
/**
 * Created by aanwer on 11/11/2016.
 */

@ParseClassName("Kid")
public class Kid extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getMobileNumber() {
        return getString("mobileNumber");
    }

    public void setMobileNumber(String mobileNumber) {
        put("mobileNumber", mobileNumber);
    }

    public String getHomeNumber() {
        return getString("homeNumber");
    }

    public void setHomeNumber(String homeNumber) {
        put("homeNumber", homeNumber);
    }

    public String getFatherNumber() {
        return getString("fatherNumber");
    }

    public void setFatherNumber(String fatherNumber) {
        put("fatherNumber", fatherNumber);
    }

    public String getMotherNumber() {
        return getString("motherNumber");
    }

    public void setMotherNumber(String motherNumber) {
        put("motherNumber", motherNumber);
    }

    public String getGrandNumber() {
        return getString("grandNumber");
    }

    public void setGrandNumber(String grandNumber) {
        put("grandNumber", grandNumber);
    }

    public String getExtraNumber1() {
        return getString("extraNumber1");
    }

    public void setExtraNumber1(String extraNumber1) {
        put("extraNumber1", extraNumber1);
    }

    public String getExtraNumber2() {
        return getString("extraNumber2");
    }

    public void setExtraNumber2(String extraNumber2) {
        put("extraNumber2", extraNumber2);
    }

    public String getStreetName() {
        return getString("streetName");
    }

    public void setStreetName(String streetName) {
        put("streetName", streetName);
    }

    public String getAreaName() {
        return getString("areaName");
    }

    public void setAreaName(String areaName) {
        put("areaName", areaName);
    }

    public String getBuildingNumber() {
        return getString("buildingNumber");
    }

    public void setBuildingNumber(String buildingNumber) {
        put("buildingNumber", buildingNumber);
    }

    public String getApartmentNumber() {
        return getString("apartmentNumber");
    }

    public void setApartmentNumber(String apartmentNumber) {
        put("apartmentNumber", apartmentNumber);
    }

    public String getFloorNumber() {
        return getString("floorNumber");
    }

    public void setFloorNumber(String floorNumber) {
        put("floorNumber", floorNumber);
    }

    public String getAddressSpecialSign() {
        return getString("addressSpecialSign");
    }

    public void setAddressSpecialSign(String addressSpecialSign) {
        put("addressSpecialSign", addressSpecialSign);
    }

    public String getOtherAddress() {
        return getString("otherAddress");
    }

    public void setOtherAddress(String otherAddress) {
        put("otherAddress", otherAddress);
    }

    public String getComment() {
        return getString("comment");
    }

    public void setComment(String comment) {
        put("comment", comment);
    }

    public String getConfessionFather() {
        return getString("confessionFather");
    }

    public void setConfessionFather(String confessionFather) {
        put("confessionFather", confessionFather);
    }

    public String getSchool() {
        return getString("school");
    }

    public void setSchool(String school) {
        put("school", school);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getBirthday() {
        return getString("birthday");
    }

    public void setBirthday(String birthday) {
        put("birthday", birthday);
    }

    public void setImage (ParseFile image){
        put("image",image);
    }
    public ParseFile getImage(){
        return getParseFile("image");
    }
    public void setThumbNail (ParseFile thumbNail){
        put("thumbNail",thumbNail);
    }
    public ParseFile getThumbNail(){
        return getParseFile("thumbNail");
    }

   public void setNumberOfVisits(String visits){
       put("visits",visits);
   }

    public String getNumberOfVisits(){
        return getString("visits");
    }
    public void setNumberOfAttendances(String attendances){
        put("attendances",attendances);
    }
    public String getNumberOfAttendances(){
        return getString("attendances");
    }
    public void setLastTimeAttendance(boolean lastTimeAttendance){
        put ("lastTimeAttendance", lastTimeAttendance);
    }
    public boolean getLastTimeAttendance(){
        return getBoolean("lastTimeAttendance");
    }
}




