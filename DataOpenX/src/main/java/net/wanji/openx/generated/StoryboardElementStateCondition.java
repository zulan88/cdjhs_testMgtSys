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
 * <p>StoryboardElementStateCondition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="StoryboardElementStateCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="storyboardElementRef" use="required" type="{}String" />
 *       attribute name="state" use="required" type="{}StoryboardElementState" />
 *       attribute name="storyboardElementType" use="required" type="{}StoryboardElementType" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StoryboardElementStateCondition")
public class StoryboardElementStateCondition {

    @XmlAttribute(name = "storyboardElementRef", required = true)
    protected String storyboardElementRef;
    @XmlAttribute(name = "state", required = true)
    protected String state;
    @XmlAttribute(name = "storyboardElementType", required = true)
    protected String storyboardElementType;

    /**
     * ��ȡstoryboardElementRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoryboardElementRef() {
        return storyboardElementRef;
    }

    /**
     * ����storyboardElementRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoryboardElementRef(String value) {
        this.storyboardElementRef = value;
    }

    /**
     * ��ȡstate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * ����state���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * ��ȡstoryboardElementType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoryboardElementType() {
        return storyboardElementType;
    }

    /**
     * ����storyboardElementType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoryboardElementType(String value) {
        this.storyboardElementType = value;
    }

}
