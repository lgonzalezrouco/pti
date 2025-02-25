const express = require("express");
const app = express();
const port = 8080;

app.use(express.json());

app.get("/", (req, res) => {
  res.send("Hello World!");
});

app.get("/students", (req, res, next) => {
  res.json({
    responseId: 1234,
    students: [
      { name: "Jordi", studentId: "12345678a" },
      { name: "Marta", studentId: "12345678b" },
    ],
  });
});

app.get("/students/:studentId", function (req, res) {
  res.send(
    "Received request at /students with param studentId=" + req.params.studentId
  );
});

app.post("/newstudent", (req, res, next) => {
  for (var i in req.body.students) {
    console.log(req.body.students[i].name + "\n");
  }
  res.status(201);
  res.end();
});

app.post("/rental", (req, res, next) => {
  const fs = require("fs");
  const filepath = "rentals.json";

  // Check if the file already exists
  if (!fs.existsSync(filepath)) {
    // Create an empty JSON document in memory and save it to a file (students.json)
    rentalsJSON = { rentals: [] };
    fs.writeFileSync(filepath, JSON.stringify(rentalsJSON));
  } else {
    // The file exists, let's read the JSON document into memory
    rentalsFileRawData = fs.readFileSync(filepath);
    rentalsJSON = JSON.parse(rentalsFileRawData);
  }

  // Add something to the (in memory) JSON document
  rentalsJSON["rentals"].push({
    maker: req.body.maker,
    model: req.body.model,
    days: req.body.days,
    units: req.body.units,
  });

  // Write the (modified) JSON document into a file
  fs.writeFileSync(filepath, JSON.stringify(rentalsJSON));

  res.status(201);
  res.end();
});

app.get("/rentals", (req, res, next) => {
  const fs = require("fs");
  const filepath = "rentals.json";

  // Check if the file already exists
  if (!fs.existsSync(filepath)) {
    // Create an empty JSON document in memory and save it to a file (students.json)
    rentalsJSON = { rentals: [] };
    fs.writeFileSync(filepath, JSON.stringify(rentalsJSON));
  } else {
    // The file exists, let's read the JSON document into memory
    rentalsFileRawData = fs.readFileSync(filepath);
    rentalsJSON = JSON.parse(rentalsFileRawData);
  }

  res.json(rentalsJSON);
});

app.listen(port, () => {
  console.log(`PTI HTTP Server listening at http://localhost:${port}`);
});
