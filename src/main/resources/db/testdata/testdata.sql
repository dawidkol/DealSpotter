INSERT INTO category
    (name)
VALUES ('Wszystkie kategorie'),
       ('Elektronika'),
       ('Moda'),
       ('Dom i Ogród'),
       ('Supermarket'),
       ('Dziecko'),
       ('Uroda'),
       ('Zdrowie'),
       ('Kultura i rozrywka'),
       ('Sport i turystyka'),
       ('Motoryzacja'),
       ('Nieruchomości');
INSERT INTO promo
    (name, description, price, url_address, category_id, user_id, added)
VALUES ('Apple MacBook Air M1',
        'MacBook Air z M1 taczy megawydajnosé z niewiarygodnie atrakcyjna
   cena. Jest niesamowicie poręczny, szybki i sprawny, ma bezszelestna
   konstrukcje pozbawiona wentylatora oraz piekny wyswietlacz Retina.
   Smuklego, lekkiego jak piórko MacBooka Air z bateria na caly dzień nie
   soowoinia nawei naiciezsze zacania', 5899,
        'https://www.x-kom.pl/p/606361-notebook-laptop-133-apple-macbook-air-m1-16gb-256-mac-os-silver.html', 2, 1, now()),

       ('Apple MacBook Air M2',
        'Przeprojektowany pod kątem czipa M2 nowej generacji, niewiarygodnie smukły MacBook Air
w wytrzymałej aluminiowej obudowie zachwyca energooszczędnością i szybkością działania. To
ultraprzenośny, superwszechstronny laptop do pracy, zabawy i tworzenia wszystkiego, czego ', 5899,
        'https://www.x-kom.pl/p/1054824-notebook-laptop-133-apple-macbook-air-m2-16gb-256-mac-os-silver.html', 2, 2, now()),

       ('Yeelight Lampa na monitor Screen Light Bar Pro RGB',
        'Skorzystaj z lampy na monitor Yeelight Screen Light Bar Pro, która zapewni Ci optymalne oświetlenie podczas' ||
        ' gry lub pracy przy komputerze. Lampa ta generuje przyjazne dla oczu światło, które możesz dostosować do własnych potrzeb.
   Lampa oferuje kompatybilność z Razer Chroma, dzięki czemu możesz cieszyć się wciągającą rozgrywką i doskonałą imersją.' ||
        ' Oświetleniem będziesz mógł sterować za pomocą aplikacji Yeelight lub poprzez asystenta głosowego Asystent Google,' ||
        ' lub Amazon Alexa. Lampa Screen Light Bar Pro jest również kompatybilna z Samsung SmartThings. ' ||
        'Co więcej, obsługę umożliwi Ci również dołączony do zestawu kontroler zdalnego sterowania.',
        299, 'https://www.x-kom.pl/p/721440-inteligentna-lampa-yeelight-lampa-na-monitor-screen-light-bar-pro-rgb.html',
        2, 3, now()),

       ('Czapka męska z daszkiem 4F Bejsbolówka 2023 L',
        'Uniwersalna, markowa czapka z daszkiem renomowanej marki 4F, najnowszej kolekcji WIOSNA/LATO 2023.',
        44.90,
        'https://allegro.pl/oferta/czapka-meska-z-daszkiem-4f-bejsbolowka-2023-l-13492195285?bi_s=ads&bi_m=productlisting:desktop:query&bi_c=NTUwOGI0ZjItMWQ3OS00ZThlLWI0MGEtMjEzY2ZmMWU2MTFlAA&bi_t=ape&referrer=proxy&emission_unit_id=7b25b5b9-54d8-4b86-a74f-65afd3a46524',
        3, 2, now()),

       ('Tabliczka Tablica ADRESOWA ALUMINIOWA - Numer domu',
        'Tabliczka drukowana metodą UV na lekkim ' ||
        'i wytrzymałym  materiale DIBOND . Technologia UV ' ||
        'jest najtrwalszą metodą druku co sprawia,' ||
        ' że kolory pozostają nienaruszone przez wiele lat.' ||
        ' Nadruk odporny na działanie światła i warunków atmosferycznych.' ||
        'Grafika w wysokiej jakości i rewelacyjnej cenie.', 40,
        'https://allegro.pl/oferta/tablica-tabliczka-adresowa-numer-domu-aluminiowa-11356934049?bi_s=ads&bi_m=productlisting:desktop:query&bi_c=NDk3ODAxZjgtNzc5YS00MTNjLWJhZDItMzBlNjJkMDczZDc3AA&bi_t=ape&referrer=proxy&emission_unit_id=eae55830-0019-4cb0-a2c2-a69b720f48ec',
        4, 2, now()),

       ('Kawa cappuccino Jacobs',
        'Kawa rozpuszczalna Jacobs Cappuccino Caramel to ulubiona kawa rozpuszczalna ' ||
        'Jacobs wzbogacona o smak i aromat karmelu. Sprawdzi się o każdej porze, kiedy poczujesz chęć ' ||
        'na "coś słodkigo", a dzięki wygodnym saszetkom taki deser przygotujesz w mgnieniu oka!',
        11.90,
        'https://allegro.pl/oferta/kawa-rozpuszczalna-jacobs-cappuccino-caramel-10790745595?bi_s=ads&bi_m=productlisting:desktop:query&bi_c=NDU0NjNiMGItMGUwMS00MWIyLWJiNmMtN2ViNWEzZWU2ZmE5AA&bi_t=ape&referrer=proxy&emission_unit_id=d00e0c7d-fbee-4f5d-91c5-b6612b12a280',
        5, 1, now());

INSERT INTO user_role
    (name, description)
VALUES ('ADMIN', 'Zarządzanie treścią, zarządzanie użytkownikami, dodawanie treści'),
       ('USER', 'Dodawanie treści');

INSERT INTO application_user(first_name, last_name, email, password)
VALUES ('admin', 'admin', 'admin@admin.pl', '{noop}admin'),
       ('Mateusz', 'Kowalski', 'mateusz.kowalski@abc.pl', '{noop}simplePass'),
       ('Stefan', 'Zabłocki', 'stefan.zablocki@abc.pl', '{noop}simplePass');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 2);