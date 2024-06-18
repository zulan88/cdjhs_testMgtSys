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
 * <p>PositionInLaneCoordinates enity
 * 
 * 
 * 
 * <pre>
 * complexType name="PositionInLaneCoordinates">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="laneId" use="required" type="{}String" />
 *       attribute name="laneOffset" type="{}Double" />
 *       attribute name="pathS" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PositionInLaneCoordinates")
public class PositionInLaneCoordinates {

    @XmlAttribute(name = "laneId", required = true)
    protected String laneId;
    @XmlAttribute(name = "laneOffset")
    protected String laneOffset;
    @XmlAttribute(name = "pathS", required = true)
    protected String pathS;

    /**
     * ��ȡlaneId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLaneId() {
        return laneId;
    }

    /**
     * ����laneId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLaneId(String value) {
        this.laneId = value;
    }

    /**
     * ��ȡlaneOffset���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLaneOffset() {
        return laneOffset;
    }

    /**
     * ����laneOffset���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLaneOffset(String value) {
        this.laneOffset = value;
    }

    /**
     * ��ȡpathS���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathS() {
        return pathS;
    }

    /**
     * ����pathS���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathS(String value) {
        this.pathS = value;
    }

}
