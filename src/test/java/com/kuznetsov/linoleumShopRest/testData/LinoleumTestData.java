package com.kuznetsov.linoleumShopRest.testData;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.RevisionDto;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.entity.Revision;
import org.hibernate.envers.RevisionType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;


public final class LinoleumTestData {

    public static final String BUCKET = System.getProperty("user.dir")+"/image";

    public static final Integer NEWID = 6;

    public static final Linoleum LINOLEUM =
            Linoleum.builder()
            .lName("saratoga 4")
            .protect(0.4f)
            .thickness(2f)
            .price(940)
            .imagePath("test.png")
            .orders(Collections.emptyList())
            .rolls(Collections.emptyList())
            .build();

    private static final Linoleum LINOLEUM_IN_REVISION =
            Linoleum.builder()
                    .lName("saratoga 4")
                    .protect(0.4f)
                    .thickness(2f)
                    .price(730)
                    .imagePath("test.png")
                    .orders(Collections.emptyList())
                    .rolls(Collections.emptyList())
                    .build();


    private static final Linoleum EMPTY_LINOLEUM_IN_REVISION =
            new Linoleum();

    public static final Linoleum DB_EXISTED_LINOLEUM =
            Linoleum.builder()
                    .lName("jane 4")
                    .protect(0.5f)
                    .thickness(3.5f)
                    .price(850)
                    .imagePath("test.png")
                    .orders(Collections.emptyList())
                    .rolls(Collections.emptyList())
                    .build();

    public static final List<ReadLinoleumDto> FILTERED_LINOLEUM_READ_DTO =
            List.of(new ReadLinoleumDto(3,"baden 1",0.5f,2f,710,"src/main/resources/images/BADEN-1-300x300.jpg"),
                    new ReadLinoleumDto(2,"duart 1",0.4f,2.7f,600,"src/main/resources/images/SPENSER-3-300x300.jpg")
            );

    public static final JSONObject JSON = new JSONObject();
    public static final JSONObject EXISTED_JSON = new JSONObject();
    public static final JSONObject LINOLEUM_FILTER_JSON = new JSONObject();
    public static final JSONObject FILTERED_LINOLEUM_CONTENT_JSON = new JSONObject();
    public static final JSONArray FILTERED_LINOLEUM_DTO_ARRAY_JSON = new JSONArray();
    public static final JSONObject FILTERED_LINOLEUM_METADATA_JSON = new JSONObject();
    public static final JSONObject FILTERED_LINOLEUM_DTO1_JSON = new JSONObject();
    public static final JSONObject FILTERED_LINOLEUM_DTO2_JSON = new JSONObject();

    static {
        LINOLEUM.setId(NEWID);
        LINOLEUM_IN_REVISION.setId(6);
        EMPTY_LINOLEUM_IN_REVISION.setId(6);
        DB_EXISTED_LINOLEUM.setId(1);
        try {
            JSON.put("lName",LINOLEUM.getLName());
            JSON.put("protect",LINOLEUM.getProtect());
            JSON.put("thickness",LINOLEUM.getThickness());
            JSON.put("price",LINOLEUM.getPrice());

            LINOLEUM_FILTER_JSON.put("name","");
            LINOLEUM_FILTER_JSON.put("protect","");
            LINOLEUM_FILTER_JSON.put("thickness","");
            LINOLEUM_FILTER_JSON.put("maxPrice",711);
            LINOLEUM_FILTER_JSON.put("minPrice",599);

            EXISTED_JSON.put("id",DB_EXISTED_LINOLEUM.getId());
            EXISTED_JSON.put("lName",DB_EXISTED_LINOLEUM.getLName());
            EXISTED_JSON.put("protect",DB_EXISTED_LINOLEUM.getProtect());
            EXISTED_JSON.put("thickness",DB_EXISTED_LINOLEUM.getThickness());
            EXISTED_JSON.put("price",DB_EXISTED_LINOLEUM.getPrice());
            EXISTED_JSON.put("imagePath",DB_EXISTED_LINOLEUM.getImagePath());

            FILTERED_LINOLEUM_DTO1_JSON.put("id",FILTERED_LINOLEUM_READ_DTO.get(0).getId());
            FILTERED_LINOLEUM_DTO1_JSON.put("lName",FILTERED_LINOLEUM_READ_DTO.get(0).getLName());
            FILTERED_LINOLEUM_DTO1_JSON.put("protect",FILTERED_LINOLEUM_READ_DTO.get(0).getProtect());
            FILTERED_LINOLEUM_DTO1_JSON.put("thickness",FILTERED_LINOLEUM_READ_DTO.get(0).getThickness());
            FILTERED_LINOLEUM_DTO1_JSON.put("price",FILTERED_LINOLEUM_READ_DTO.get(0).getPrice());
            FILTERED_LINOLEUM_DTO1_JSON.put("imagePath",FILTERED_LINOLEUM_READ_DTO.get(0).getImagePath());

            FILTERED_LINOLEUM_DTO2_JSON.put("id",FILTERED_LINOLEUM_READ_DTO.get(1).getId());
            FILTERED_LINOLEUM_DTO2_JSON.put("lName",FILTERED_LINOLEUM_READ_DTO.get(1).getLName());
            FILTERED_LINOLEUM_DTO2_JSON.put("protect",FILTERED_LINOLEUM_READ_DTO.get(1).getProtect());
            FILTERED_LINOLEUM_DTO2_JSON.put("thickness",FILTERED_LINOLEUM_READ_DTO.get(1).getThickness());
            FILTERED_LINOLEUM_DTO2_JSON.put("price",FILTERED_LINOLEUM_READ_DTO.get(1).getPrice());
            FILTERED_LINOLEUM_DTO2_JSON.put("imagePath",FILTERED_LINOLEUM_READ_DTO.get(1).getImagePath());

            FILTERED_LINOLEUM_DTO_ARRAY_JSON.put(FILTERED_LINOLEUM_DTO1_JSON)
                            .put(FILTERED_LINOLEUM_DTO2_JSON);

            FILTERED_LINOLEUM_METADATA_JSON.put("page",0);
            FILTERED_LINOLEUM_METADATA_JSON.put("size",2);
            FILTERED_LINOLEUM_METADATA_JSON.put("totalElements",2);
            FILTERED_LINOLEUM_CONTENT_JSON.put("content",FILTERED_LINOLEUM_DTO_ARRAY_JSON);
            FILTERED_LINOLEUM_CONTENT_JSON.put("metadata",FILTERED_LINOLEUM_METADATA_JSON);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static final CreateEditLinoleumDto CREATE_EDIT_LINOLEUM_DTO =
            new CreateEditLinoleumDto(LINOLEUM.getLName(),
                    LINOLEUM.getProtect(),
                    LINOLEUM.getThickness(),
                    LINOLEUM.getPrice(),
                    getMockImage());

    public static final ReadLinoleumDto READ_LINOLEUM_DTO =
            new ReadLinoleumDto(NEWID,
                    LINOLEUM.getLName(),
                    LINOLEUM.getProtect(),
                    LINOLEUM.getThickness(),
                    LINOLEUM.getPrice(),
                    LINOLEUM.getImagePath());

    public static final ReadLinoleumDto EXISTED_READ_LINOLEUM_DTO =
            new ReadLinoleumDto(1,
                    DB_EXISTED_LINOLEUM.getLName(),
                    DB_EXISTED_LINOLEUM.getProtect(),
                    DB_EXISTED_LINOLEUM.getThickness(),
                    DB_EXISTED_LINOLEUM.getPrice(),
                    DB_EXISTED_LINOLEUM.getImagePath());

    public static final Path IMAGE_FULL_PATH =
            Path.of(BUCKET, getMockImage().getOriginalFilename());

    public static final Path UPDATED_IMAGE_FULL_PATH =
            Path.of(BUCKET, getMockImageForUpdate().getOriginalFilename());

    public static final List<ReadLinoleumDto> ALL_LINOLEUM_READ_DTO =
            List.of(new ReadLinoleumDto(1,"jane 4",0.5f,3.5f,850,"test.png"),
                    new ReadLinoleumDto(2,"duart 1",0.4f,2.7f,600,"src/main/resources/images/SPENSER-3-300x300.jpg"),
                    new ReadLinoleumDto(3,"baden 1",0.5f,2f,710,"src/main/resources/images/BADEN-1-300x300.jpg"),
                    new ReadLinoleumDto(4,"forest 3",0.4f,4.7f,800,"src/main/resources/images/saratoga-6-300x300.jpg"),
                    new ReadLinoleumDto(5,"rigard 4",0.15f,2f,400,"src/main/resources/images/tango-3-1-300x300.jpg")
            );

    public static final List<RevisionDto> ALL_REVISIONS =
            List.of(new RevisionDto(LINOLEUM,new Revision(1L,1L), RevisionType.ADD),
                    new RevisionDto(LINOLEUM_IN_REVISION,new Revision(2L,1L), RevisionType.MOD),
                    new RevisionDto(EMPTY_LINOLEUM_IN_REVISION,new Revision(3L,1L), RevisionType.DEL)
            );

    public static final String BASE_URI = "/api/v1/linoleums";

    public static final MockMultipartFile JSON_FILE =
            new MockMultipartFile("json",
                    "",
                    "application/json",
                    JSON.toString().getBytes());


    public static MockMultipartFile getMockImage(){
        MockMultipartFile image = null;
        try {
            FileInputStream fileInputStream =
                    new FileInputStream(System.getProperty("user.dir")+"/src/test/java/com/kuznetsov/linoleumShopRest/image/test.png");
            image = new MockMultipartFile("image",
                            "test.png",
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                            fileInputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public static MockMultipartFile getMockImageForUpdate(){
        MockMultipartFile image = null;
        try {
            FileInputStream fileInputStream =
                    new FileInputStream(System.getProperty("user.dir")+"/src/test/java/com/kuznetsov/linoleumShopRest/image/updated.png");
            image = new MockMultipartFile("image",
                    "updated.png",
                    "multipart/form-data",
                    fileInputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public static void flushLinoleum(Linoleum linoleum){
        linoleum.setId(6);
        linoleum.setLName("saratoga 4");
        linoleum.setProtect(0.4f);
        linoleum.setThickness(2f);
        linoleum.setPrice(940);
        linoleum.setImagePath("test.png");
        linoleum.setRolls(Collections.emptyList());
        linoleum.setOrders(Collections.emptyList());
    }

    public static void flushCreateEditLinoleumDto(CreateEditLinoleumDto createEditLinoleumDto){
        createEditLinoleumDto.setLName("saratoga 4");
        createEditLinoleumDto.setProtect(0.4f);
        createEditLinoleumDto.setThickness(2f);
        createEditLinoleumDto.setPrice(940);
        createEditLinoleumDto.setImage(getMockImage());
    }

    public static void flushImage() throws IOException {
        if(Files.exists(IMAGE_FULL_PATH)) {
            Files.delete(IMAGE_FULL_PATH);
        }

        if(Files.exists(UPDATED_IMAGE_FULL_PATH)) {
            Files.delete(UPDATED_IMAGE_FULL_PATH);
        }
    }

}
