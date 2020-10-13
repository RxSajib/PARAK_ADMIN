package com.rakpak.pak_parak_admin.Model;

public class Help {

  private String Name, Mobile,ID,FamilyMembers, Work, Location, MontlyIncome, HelpDetails;
  private String WhyNeedHelp,  DownloadPdf;


  public Help() {
  }

  public Help(String name, String mobile, String ID, String familyMembers, String work, String location, String montlyIncome, String helpDetails, String whyNeedHelp, String downloadPdf) {
    Name = name;
    Mobile = mobile;
    this.ID = ID;
    FamilyMembers = familyMembers;
    Work = work;
    Location = location;
    MontlyIncome = montlyIncome;
    HelpDetails = helpDetails;
    WhyNeedHelp = whyNeedHelp;
    DownloadPdf = downloadPdf;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getMobile() {
    return Mobile;
  }

  public void setMobile(String mobile) {
    Mobile = mobile;
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getFamilyMembers() {
    return FamilyMembers;
  }

  public void setFamilyMembers(String familyMembers) {
    FamilyMembers = familyMembers;
  }

  public String getWork() {
    return Work;
  }

  public void setWork(String work) {
    Work = work;
  }

  public String getLocation() {
    return Location;
  }

  public void setLocation(String location) {
    Location = location;
  }

  public String getMontlyIncome() {
    return MontlyIncome;
  }

  public void setMontlyIncome(String montlyIncome) {
    MontlyIncome = montlyIncome;
  }

  public String getHelpDetails() {
    return HelpDetails;
  }

  public void setHelpDetails(String helpDetails) {
    HelpDetails = helpDetails;
  }

  public String getWhyNeedHelp() {
    return WhyNeedHelp;
  }

  public void setWhyNeedHelp(String whyNeedHelp) {
    WhyNeedHelp = whyNeedHelp;
  }

  public String getDownloadPdf() {
    return DownloadPdf;
  }

  public void setDownloadPdf(String downloadPdf) {
    DownloadPdf = downloadPdf;
  }
}
