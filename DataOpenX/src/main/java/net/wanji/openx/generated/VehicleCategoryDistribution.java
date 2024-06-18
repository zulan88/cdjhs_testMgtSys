//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>VehicleCategoryDistribution enity
 * 
 *
 * 
 * <pre>
 * complexType name="VehicleCategoryDistribution">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="VehicleCategoryDistributionEntry" type="{}VehicleCategoryDistributionEntry" maxOccurs="unbounded"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehicleCategoryDistribution", propOrder = {
    "vehicleCategoryDistributionEntry"
})
public class VehicleCategoryDistribution {

    @XmlElement(name = "VehicleCategoryDistributionEntry", required = true)
    protected List<VehicleCategoryDistributionEntry> vehicleCategoryDistributionEntry;

    /**
     * Gets the value of the vehicleCategoryDistributionEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vehicleCategoryDistributionEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVehicleCategoryDistributionEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VehicleCategoryDistributionEntry }
     * 
     * 
     */
    public List<VehicleCategoryDistributionEntry> getVehicleCategoryDistributionEntry() {
        if (vehicleCategoryDistributionEntry == null) {
            vehicleCategoryDistributionEntry = new ArrayList<VehicleCategoryDistributionEntry>();
        }
        return this.vehicleCategoryDistributionEntry;
    }

}
