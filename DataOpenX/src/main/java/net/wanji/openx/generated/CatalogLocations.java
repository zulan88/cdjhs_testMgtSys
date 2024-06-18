//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>CatalogLocations enity
 * 
 * 
 * 
 * <pre>
 * complexType name="CatalogLocations">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="VehicleCatalog" type="{}VehicleCatalogLocation" minOccurs="0"/>
 *         element name="ControllerCatalog" type="{}ControllerCatalogLocation" minOccurs="0"/>
 *         element name="PedestrianCatalog" type="{}PedestrianCatalogLocation" minOccurs="0"/>
 *         element name="MiscObjectCatalog" type="{}MiscObjectCatalogLocation" minOccurs="0"/>
 *         element name="EnvironmentCatalog" type="{}EnvironmentCatalogLocation" minOccurs="0"/>
 *         element name="ManeuverCatalog" type="{}ManeuverCatalogLocation" minOccurs="0"/>
 *         element name="TrajectoryCatalog" type="{}TrajectoryCatalogLocation" minOccurs="0"/>
 *         element name="RouteCatalog" type="{}RouteCatalogLocation" minOccurs="0"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CatalogLocations", propOrder = {

})
public class CatalogLocations {

    @XmlElement(name = "VehicleCatalog")
    protected VehicleCatalogLocation vehicleCatalog;
    @XmlElement(name = "ControllerCatalog")
    protected ControllerCatalogLocation controllerCatalog;
    @XmlElement(name = "PedestrianCatalog")
    protected PedestrianCatalogLocation pedestrianCatalog;
    @XmlElement(name = "MiscObjectCatalog")
    protected MiscObjectCatalogLocation miscObjectCatalog;
    @XmlElement(name = "EnvironmentCatalog")
    protected EnvironmentCatalogLocation environmentCatalog;
    @XmlElement(name = "ManeuverCatalog")
    protected ManeuverCatalogLocation maneuverCatalog;
    @XmlElement(name = "TrajectoryCatalog")
    protected TrajectoryCatalogLocation trajectoryCatalog;
    @XmlElement(name = "RouteCatalog")
    protected RouteCatalogLocation routeCatalog;

    /**
     * ��ȡvehicleCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link VehicleCatalogLocation }
     *     
     */
    public VehicleCatalogLocation getVehicleCatalog() {
        return vehicleCatalog;
    }

    /**
     * ����vehicleCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleCatalogLocation }
     *     
     */
    public void setVehicleCatalog(VehicleCatalogLocation value) {
        this.vehicleCatalog = value;
    }

    /**
     * ��ȡcontrollerCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ControllerCatalogLocation }
     *     
     */
    public ControllerCatalogLocation getControllerCatalog() {
        return controllerCatalog;
    }

    /**
     * ����controllerCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ControllerCatalogLocation }
     *     
     */
    public void setControllerCatalog(ControllerCatalogLocation value) {
        this.controllerCatalog = value;
    }

    /**
     * ��ȡpedestrianCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PedestrianCatalogLocation }
     *     
     */
    public PedestrianCatalogLocation getPedestrianCatalog() {
        return pedestrianCatalog;
    }

    /**
     * ����pedestrianCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PedestrianCatalogLocation }
     *     
     */
    public void setPedestrianCatalog(PedestrianCatalogLocation value) {
        this.pedestrianCatalog = value;
    }

    /**
     * ��ȡmiscObjectCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MiscObjectCatalogLocation }
     *     
     */
    public MiscObjectCatalogLocation getMiscObjectCatalog() {
        return miscObjectCatalog;
    }

    /**
     * ����miscObjectCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MiscObjectCatalogLocation }
     *     
     */
    public void setMiscObjectCatalog(MiscObjectCatalogLocation value) {
        this.miscObjectCatalog = value;
    }

    /**
     * ��ȡenvironmentCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EnvironmentCatalogLocation }
     *     
     */
    public EnvironmentCatalogLocation getEnvironmentCatalog() {
        return environmentCatalog;
    }

    /**
     * ����environmentCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EnvironmentCatalogLocation }
     *     
     */
    public void setEnvironmentCatalog(EnvironmentCatalogLocation value) {
        this.environmentCatalog = value;
    }

    /**
     * ��ȡmaneuverCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ManeuverCatalogLocation }
     *     
     */
    public ManeuverCatalogLocation getManeuverCatalog() {
        return maneuverCatalog;
    }

    /**
     * ����maneuverCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ManeuverCatalogLocation }
     *     
     */
    public void setManeuverCatalog(ManeuverCatalogLocation value) {
        this.maneuverCatalog = value;
    }

    /**
     * ��ȡtrajectoryCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrajectoryCatalogLocation }
     *     
     */
    public TrajectoryCatalogLocation getTrajectoryCatalog() {
        return trajectoryCatalog;
    }

    /**
     * ����trajectoryCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrajectoryCatalogLocation }
     *     
     */
    public void setTrajectoryCatalog(TrajectoryCatalogLocation value) {
        this.trajectoryCatalog = value;
    }

    /**
     * ��ȡrouteCatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RouteCatalogLocation }
     *     
     */
    public RouteCatalogLocation getRouteCatalog() {
        return routeCatalog;
    }

    /**
     * ����routeCatalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RouteCatalogLocation }
     *     
     */
    public void setRouteCatalog(RouteCatalogLocation value) {
        this.routeCatalog = value;
    }

}
