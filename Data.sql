-- Category
-- DELETE FROM category
INSERT INTO category VALUES ('CAT0001','Pop')
INSERT INTO category VALUES ('CAT0002','Rap')
INSERT INTO category VALUES ('CAT0003','Rock')
INSERT INTO category VALUES ('CAT0004','K-Pop')
INSERT INTO category VALUES ('CAT0005','Jazz')
INSERT INTO category VALUES ('CAT0006','Holiday')
INSERT INTO category VALUES ('CAT0007','Party')

-- Role
-- DELETE FROM role
INSERT INTO [role] VALUES ('admin')
INSERT INTO [role] VALUES ('moderator')
INSERT INTO [role] VALUES ('member')


-- Account
-- DELETE FROM account
INSERT INTO account VALUES ('bao','123','admin')
INSERT INTO account VALUES ('huy','123','admin')
INSERT INTO account VALUES ('khanh','123','admin')
INSERT INTO account VALUES ('loi','123','admin')
INSERT INTO account VALUES ('dat','123','admin')
INSERT INTO account VALUES ('minh','123','admin')
INSERT INTO account VALUES ('khue','123','admin')
INSERT INTO account VALUES ('noname','123','moderator')
INSERT INTO account VALUES ('nguoiyeudatker','123','member')

-- User
-- DELETE FROM [user]
INSERT INTO [user] VALUES ('U0001',null,'M',null,'Bùi Thanh Bảo','bao')
INSERT INTO [user] VALUES ('U0002',null,'M',null,'Trần Nhật Huy','huy')
INSERT INTO [user] VALUES ('U0003',null,'F',null,'Ngô Hiểu Khánh','khanh')
INSERT INTO [user] VALUES ('U0004',null,'M',null,'Phan Hữu Lợi','loi')
INSERT INTO [user] VALUES ('U0005',null,'M',null,'Nguyễn Đức Đạt','dat')
INSERT INTO [user] VALUES ('U0006',null,'M',null,'Nguyễn Tuấn Minh','minh')
INSERT INTO [user] VALUES ('U0007',null,'M',null,'Nguyễn Hà Khuê','khue')
INSERT INTO [user] VALUES ('U0008',null,'O',null,'Vô danh tiểu tốt','noname')
INSERT INTO [user] VALUES ('U0009',null,'F',null,'Bồ của đạtker','nguoiyeudatker')

-- Artist
-- DELETE FROM artist
INSERT INTO [artist] VALUES ('ART0001','JOY','???')
INSERT INTO [artist] VALUES ('ART0002','Charlie Puth','???')
INSERT INTO [artist] VALUES ('ART0003','Sơn Tùng MTP','???')
INSERT INTO [artist] VALUES ('ART0004','Bích Phương','???')
INSERT INTO [artist] VALUES ('ART0005','Andiez','???')
INSERT INTO [artist] VALUES ('ART0006','Hoà Minzy','???')

-- Song
-- DELETE FROM song
INSERT INTO song VALUES ('SO0001',null,null,2000,null,'Hello',50000,'U0002')
INSERT INTO song VALUES ('SO0002',null,null,5000,null,'Hãy trao cho anh',50000,'U0001')
INSERT INTO song VALUES ('SO0003',null,null,2000,null,'Suýt nữa thì',2000,'U0002')
INSERT INTO song VALUES ('SO0004',null,null,2000,null,'What if i was nothing',50000,'U0003')
INSERT INTO song VALUES ('SO0005',null,null,2000,null,'Attention',70000,'U0004')
INSERT INTO song VALUES ('SO0006',null,null,2000,null,'Billie Jean',22000,'U0005')
INSERT INTO song VALUES ('SO0007',null,null,2000,null,'Nàng thơ',31000,'U0006')
INSERT INTO song VALUES ('SO0008',null,null,2000,null,'Em gái mưa',2400,'U0009')
INSERT INTO song VALUES ('SO0009',null,null,12200,null,'Rời bỏ',500,'U0004')

--Song & Category
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0004','SO0001')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0003','SO0002')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0005','SO0003')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0006','SO0004')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0007','SO0005')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0001','SO0006')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0002','SO0007')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0003','SO0008')
INSERT INTO song_category(category_id,song_id) VALUES ('CAT0002','SO0009')

--Song & Artist
INSERT INTO song_artist VALUES ('SO0001','ART0001')
INSERT INTO song_artist VALUES ('SO0002','ART0002')
INSERT INTO song_artist VALUES ('SO0003','ART0003')
INSERT INTO song_artist VALUES ('SO0004','ART0004')
INSERT INTO song_artist VALUES ('SO0005','ART0005')
INSERT INTO song_artist VALUES ('SO0006','ART0006')
INSERT INTO song_artist VALUES ('SO0007','ART0002')
INSERT INTO song_artist VALUES ('SO0008','ART0003')
INSERT INTO song_artist VALUES ('SO0009','ART0004')

--Playlist
INSERT INTO playlist VALUES ('PL0001',2000,'abc','Test playlist','U0001')

SELECT * FROM song
DELETE FROM song WHERE song_id = 'SO0007'
-- SELECT * FROM category
SELECT * FROM song_category
SELECT * FROM song_artist
SELECT * FROM playlist_song
SELECT * FROM [user]


CREATE DATABASE nhacmoi
DROP DATABASE nhacmoi
SELECT * FROM song_category
SELECT * FROM account
SELECT * FROM category
SELECT * FROM artist
SELECT * FROM playlist