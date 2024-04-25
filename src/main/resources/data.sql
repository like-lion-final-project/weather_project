INSERT INTO cloths_category (type)
SELECT * FROM (SELECT '상의' AS type UNION ALL
               SELECT '티셔츠' UNION ALL
               SELECT '니트/스웨터' UNION ALL
               SELECT '셔츠/블라우스' UNION ALL
               SELECT '맨투맨' UNION ALL
               SELECT '후드' UNION ALL
               SELECT '민소매' UNION ALL
               SELECT '아우터' UNION ALL
               SELECT '카디건' UNION ALL
               SELECT '재킷' UNION ALL
               SELECT '코트' UNION ALL
               SELECT '점퍼' UNION ALL
               SELECT '바지' UNION ALL
               SELECT '청바지' UNION ALL
               SELECT '점프슈트' UNION ALL
               SELECT '원피스' UNION ALL
               SELECT '스커트' UNION ALL
               SELECT '악세서리' UNION ALL
               SELECT '머플러' UNION ALL
               SELECT '모자' UNION ALL
               SELECT '기타') AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM cloths_category WHERE type = tmp.type
);

INSERT INTO user (username)
SELECT 'test-user' WHERE NOT EXISTS (
    SELECT 1 FROM user WHERE username = 'test-user'
);



-- # INSERT INTO assistant (instructions, is_active, name, version,model,is_delete)
-- # VALUES ('You are a fashion expert who knows well about temperature and clothes. Try to recommend people the right fashion for each temperature. Please keep the answer form in JSON form as below. Please process the category in array form. Category: [Coat, T-shirt...] You have to recommend a total of six categories. Please omit the main categories such as the top and bottom. The values that enter the Input are C: Temperature, A: Age, and G: Gender. Please refer to this value.',
-- #         true,'Fashion Expert_0.0.1','0.0.1','gpt-3.5-turbo',false
-- #        ) ON DUPLICATE KEY UPDATE id=id;


