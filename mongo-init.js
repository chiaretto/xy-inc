db.createUser(
        {
            user: "admin",
            pwd: "admin",
            roles: [
                {
                    "role": "readWrite",
                    "db": "xyinc"
                },
                {
                    "role": "dbAdmin",
                    "db": "xyinc"
                },
                {
                    "role": "userAdmin",
                    "db": "xyinc"
                }
            ]
        }
);
db.createCollection("yxinc");
