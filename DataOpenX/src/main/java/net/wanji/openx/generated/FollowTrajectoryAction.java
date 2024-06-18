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
 * <p>FollowTrajectoryAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="FollowTrajectoryAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Trajectory" type="{}Trajectory" minOccurs="0"/>
 *         element name="CatalogReference" type="{}CatalogReference" minOccurs="0"/>
 *         element name="TimeReference" type="{}TimeReference"/>
 *         element name="TrajectoryFollowingMode" type="{}TrajectoryFollowingMode"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FollowTrajectoryAction", propOrder = {

})
public class FollowTrajectoryAction {

    @XmlElement(name = "Trajectory")
    protected Trajectory trajectory;
    @XmlElement(name = "CatalogReference")
    protected CatalogReference catalogReference;
    @XmlElement(name = "TimeReference", required = true)
    protected TimeReference timeReference;
    @XmlElement(name = "TrajectoryFollowingMode", required = true)
    protected TrajectoryFollowingMode trajectoryFollowingMode;

    /**
     * ��ȡtrajectory���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Trajectory }
     *     
     */
    public Trajectory getTrajectory() {
        return trajectory;
    }

    /**
     * ����trajectory���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Trajectory }
     *     
     */
    public void setTrajectory(Trajectory value) {
        this.trajectory = value;
    }

    /**
     * ��ȡcatalogReference���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CatalogReference }
     *     
     */
    public CatalogReference getCatalogReference() {
        return catalogReference;
    }

    /**
     * ����catalogReference���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CatalogReference }
     *     
     */
    public void setCatalogReference(CatalogReference value) {
        this.catalogReference = value;
    }

    /**
     * ��ȡtimeReference���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TimeReference }
     *     
     */
    public TimeReference getTimeReference() {
        return timeReference;
    }

    /**
     * ����timeReference���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TimeReference }
     *     
     */
    public void setTimeReference(TimeReference value) {
        this.timeReference = value;
    }

    /**
     * ��ȡtrajectoryFollowingMode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrajectoryFollowingMode }
     *     
     */
    public TrajectoryFollowingMode getTrajectoryFollowingMode() {
        return trajectoryFollowingMode;
    }

    /**
     * ����trajectoryFollowingMode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrajectoryFollowingMode }
     *     
     */
    public void setTrajectoryFollowingMode(TrajectoryFollowingMode value) {
        this.trajectoryFollowingMode = value;
    }

}
