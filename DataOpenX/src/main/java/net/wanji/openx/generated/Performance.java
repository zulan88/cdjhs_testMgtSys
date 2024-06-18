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
 * <p>Performance enity
 * 
 *
 * 
 * <pre>
 * complexType name="Performance">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="maxAcceleration" use="required" type="{}Double" />
 *       attribute name="maxDeceleration" use="required" type="{}Double" />
 *       attribute name="maxSpeed" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Performance")
public class Performance {

    public Performance(){}

    public Performance(String type){
        if(type.equals("default")){
            this.maxSpeed = "200";
            this.maxAcceleration = "200";
            this.maxDeceleration = "10.0";
        }
    }

    @XmlAttribute(name = "maxAcceleration", required = true)
    protected String maxAcceleration;
    @XmlAttribute(name = "maxDeceleration", required = true)
    protected String maxDeceleration;
    @XmlAttribute(name = "maxSpeed", required = true)
    protected String maxSpeed;

    /**
     * ��ȡmaxAcceleration���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxAcceleration() {
        return maxAcceleration;
    }

    /**
     * ����maxAcceleration���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxAcceleration(String value) {
        this.maxAcceleration = value;
    }

    /**
     * ��ȡmaxDeceleration���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxDeceleration() {
        return maxDeceleration;
    }

    /**
     * ����maxDeceleration���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxDeceleration(String value) {
        this.maxDeceleration = value;
    }

    /**
     * ��ȡmaxSpeed���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * ����maxSpeed���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxSpeed(String value) {
        this.maxSpeed = value;
    }

}
