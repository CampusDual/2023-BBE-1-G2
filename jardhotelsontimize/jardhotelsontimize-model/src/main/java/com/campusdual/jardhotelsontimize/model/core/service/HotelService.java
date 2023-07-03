package com.campusdual.jardhotelsontimize.model.core.service;

import com.campusdual.jardhotelsontimize.api.core.service.IHotelService;
import com.campusdual.jardhotelsontimize.model.core.dao.HotelDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ontimize.jee.common.services.user.UserInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Lazy
@Service("HotelService")
public class HotelService implements IHotelService {

    @Autowired
    private DefaultOntimizeDaoHelper daoHelper;

    @Autowired
    private HotelDao hotelDao;

    @Autowired
    private UserService userService;

    @Autowired
    private StaffService staffService;

    @Override
    @Secured({ PermissionsProviderSecured.SECURED })
    public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList) {

        EntityResult result = this.daoHelper.query(this.hotelDao,keyMap,attrList);
        if(result.toString().contains("id")) {
            result.setMessage("");

        } else {
            result.setMessage("The hotel doesn't exist");
            result.setCode(EntityResult.OPERATION_WRONG);
            result.setColumnSQLTypes(new HashMap<>());
        }

        return result;
    }

    @Override
    @Secured({ PermissionsProviderSecured.SECURED })
    public EntityResult hotelInsert(Map<String, Object> attrMap) {

        EntityResult result = new EntityResultMapImpl();

        if(attrMap.containsKey("latitude")){
            Double l = Double.parseDouble(attrMap.get("latitude").toString());

            if(l > 90 || l < -90){
                result.setMessage("Latitude range must be between -90 and +90");
                result.setCode(EntityResult.OPERATION_WRONG);
                return result;
            }
        }

        if(attrMap.containsKey("longitude")){
            Double l = Double.parseDouble(attrMap.get("longitude").toString());

            if(l > 180 || l < -180){
                result.setMessage("Longitude range must be between -180 and +180");
                result.setCode(EntityResult.OPERATION_WRONG);
                return result;
            }
        }

        try{
            result = this.daoHelper.insert(this.hotelDao,attrMap);
            result.setMessage("Successful hotel insertion");
        }catch (Exception e){
            if (e.getMessage().contains("null value")) {
                result.setMessage("All attributes must be filled");
            } else if (e.getMessage().contains("Stars must be between one and five")) {
                result.setMessage("Stars must be between one and five");
            } else if (e.getMessage().contains("\"hotel\" violates foreign key constraint \"hotel_country_fkey\"")) {
                result.setMessage("The country doesn't exist");
            } else {
                result.setMessage(e.getMessage());
            }
            result.setCode(EntityResult.OPERATION_WRONG);
        }

        return result;
    }

    @Override
    @Secured({ PermissionsProviderSecured.SECURED })
    public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) {

        List<String>attrList = new ArrayList<>();
        attrList.add("id");
        EntityResult hotelQuery = hotelQuery(keyMap, attrList);

        if(hotelQuery.getCode() == EntityResult.OPERATION_WRONG){
            hotelQuery.setMessage("Hotel not found");
            return hotelQuery;
        }



        EntityResult checkPermissions = checkPermissions(keyMap.get("id").toString());
        if(checkPermissions.getCode() == EntityResult.OPERATION_WRONG){
            return checkPermissions;
        }

        EntityResult result = new EntityResultMapImpl();

        if(attrMap.containsKey("latitude")){
            List<Double>latitudeList = (List<Double>) attrMap.get("latitude");
            for(Double l : latitudeList){
                if(l > 90 || l < -90){
                    result.setMessage("Latitude range must be between -90 and +90");
                    result.setCode(EntityResult.OPERATION_WRONG);
                    return result;
                }
            }
        }

        if(attrMap.containsKey("longitude")){
            List<Double>longitudeList = (List<Double>) attrMap.get("longitude");
            for(Double l : longitudeList){
                if(l > 180 || l < -180){
                    result.setMessage("Longitude range must be between -180 and +180");
                    result.setCode(EntityResult.OPERATION_WRONG);
                    return result;
                }
            }
        }

        try {
            result = this.daoHelper.update(this.hotelDao,attrMap,keyMap);
            if (result.getCode() == 0)
                result.setMessage("Successful hotel update");
            if (result.getCode() == 2) {
                result.setMessage("Hotel not found");
                result.setCode(EntityResult.OPERATION_WRONG);
            }
        }catch (Exception e){
            if (e.getMessage().contains("Stars must be between one and five")) {
                result.setMessage("Stars must be between one and five");
            } else if (e.getMessage().contains("\"hotel\" violates foreign key constraint \"hotel_country_fkey\"")) {
                result.setMessage("The country doesn't exist");
            } else {
                result.setMessage(e.getMessage());
            }
            result.setCode(EntityResult.OPERATION_WRONG);
        }

        return result;
    }

    private EntityResult checkPermissions(String idHotel){
        try {
            UserInformation userInformation = (UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Map<String, Object>key = new HashMap<>();
            key.put("username", userInformation.getUsername());
            List<String>attrList = new ArrayList<>();
            attrList.add("idperson");
            attrList.add("username");
            EntityResult userQuery = userService.userQuery(key, attrList);

            key = new HashMap<>();
            List<Object> ids=(List<Object>)userQuery.get("idperson");
            key.put("id", ids.get(0));
            attrList = new ArrayList<>();
            attrList.add("idhotel");
            attrList.add("job");
            attrList.add("id");

            EntityResult staffQuery = staffService.staffQuery(key, attrList);

            if(staffQuery.getCode() == 0){

                List<Object>idsHotel = (List<Object>) staffQuery.get("idhotel");
                List<Object>jobs = (List<Object>) staffQuery.get("job");

                if(jobs.get(0).toString().equals("10") && !(idsHotel.get(0).toString().equals(idHotel))){
                    EntityResult error = new EntityResultMapImpl();
                    error.setCode(EntityResult.OPERATION_WRONG);
                    error.setMessage("This hotel manager can only update the hotel " + idsHotel.get(0).toString());
                    return error;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new EntityResultMapImpl();
        }
        return new EntityResultMapImpl();
    }

    @Override
    @Secured({ PermissionsProviderSecured.SECURED })
    public EntityResult hotelDelete(Map<String, Object> keyMap) {

        List<String> attrList = new ArrayList<>();
        attrList.add("id");
        attrList.add("name");
        EntityResult query = this.daoHelper.query(this.hotelDao,keyMap,attrList);

        EntityResult result = this.daoHelper.delete(this.hotelDao,keyMap);

        if (query.toString().contains("id"))
            result.setMessage("Successful hotel delete");
        else {
            result.setMessage("Hotel not found");
            result.setCode(EntityResult.OPERATION_WRONG);
        }
        return result;
    }
}
