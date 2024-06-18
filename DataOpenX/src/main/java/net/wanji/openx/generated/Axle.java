//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Axle enity
 *
 *
 *
 * <pre>
 * complexType name="Axle">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="maxSteering" use="required" type="{}Double" />
 *       attribute name="positionX" use="required" type="{}Double" />
 *       attribute name="positionZ" use="required" type="{}Double" />
 *       attribute name="trackWidth" use="required" type="{}Double" />
 *       attribute name="wheelDiameter" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Axle")
public class Axle {

    public Axle() {
    }

    public Axle(String type) {
        if (type.equals("defaultF")) {
            this.maxSteering = "0.5";
            this.wheelDiameter = "0.5";
            this.trackWidth = "1.75";
            this.positionX = "2.8";
            this.positionZ = "0.25";
        } else if (type.equals("defaultR")) {
            this.maxSteering = "0.0";
            this.wheelDiameter = "0.5";
            this.trackWidth = "1.75";
            this.positionX = "0.0";
            this.positionZ = "0.25";
        }
    }

    @XmlAttribute(name = "maxSteering", required = true)
    protected String maxSteering;
    @XmlAttribute(name = "positionX", required = true)
    protected String positionX;
    @XmlAttribute(name = "positionZ", required = true)
    protected String positionZ;
    @XmlAttribute(name = "trackWidth", required = true)
    protected String trackWidth;
    @XmlAttribute(name = "wheelDiameter", required = true)
    protected String wheelDiameter;

    /**
     * ��ȡmaxSteering���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getMaxSteering() {
        return maxSteering;
    }

    /**
     * ����maxSteering���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMaxSteering(String value) {
        this.maxSteering = value;
    }

    /**
     * ��ȡpositionX���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getPositionX() {
        return positionX;
    }

    /**
     * ����positionX���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPositionX(String value) {
        this.positionX = value;
    }

    /**
     * ��ȡpositionZ���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getPositionZ() {
        return positionZ;
    }

    /**
     * ����positionZ���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPositionZ(String value) {
        this.positionZ = value;
    }

    /**
     * ��ȡtrackWidth���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getTrackWidth() {
        return trackWidth;
    }

    /**
     * ����trackWidth���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTrackWidth(String value) {
        this.trackWidth = value;
    }

    /**
     * ��ȡwheelDiameter���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getWheelDiameter() {
        return wheelDiameter;
    }

    /**
     * ����wheelDiameter���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWheelDiameter(String value) {
        this.wheelDiameter = value;
    }

}
