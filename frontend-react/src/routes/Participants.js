import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { EVENTS_URL } from "../constants";

function App() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [event, setEvent] = useState(null);
  const [eventName, setEventName] = useState("");
  const [eventTime, setEventTime] = useState("");
  const [eventPlace, setEventPlace] = useState("");
  const [participants, setParticipants] = useState(null);
  const [pPFirstName, setPPFirstName] = useState("");
  const [pPLastName, setPPLastName] = useState("");
  const [pPCode, setPPCode] = useState("");
  const [pPMethod, setPPMethod] = useState("");
  const [pPInfo, setPPInfo] = useState("");
  const [jPName, setJPName] = useState("");
  const [jPCode, setJPCode] = useState("");
  const [jPCount, setJPCount] = useState(1);
  const [jPMethod, setJPMethod] = useState("");
  const [jPInfo, setJPInfo] = useState("");
  const [paymentTypes, setPaymentTypes] = useState([]);
  const [selectedPersonType, setSelectedPersonType] = useState("PHYSICAL");

  async function loadMethods() {
    const url = EVENTS_URL + "ptypes";
    const options = {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    };
    fetch(url, options)
        .then(response => {
          if (response.ok) {
            response.json().then(data => {
              console.log("Payment types", data);
              setPaymentTypes(data);
            });
          }
        });
  }

  async function loadEvent(id) {
    if (id) {
      const eventUrl = EVENTS_URL + "events/" + id;
      console.log("Fetching event", eventUrl);
      const options = {
          method: "GET",
          headers: { "Content-Type": "application/json" }
      }
      fetch(eventUrl, options)
          .then(response => {
            if (response.ok) {
              response.json().then(data => {
                setEvent(data);
                setEventName(data.name);
                setEventTime(data.time);
                setEventPlace(data.place);
              });
            }
          });
    }
  }

  async function loadParticipants(id) {
    if (id) {
      const participantsUrl = EVENTS_URL + "event/" + id + "/participants";
      const options = {
          method: "GET",
          headers: { "Content-Type": "application/json" }
      }
      fetch(participantsUrl, options)
          .then(response => {
            if (response.ok) {
              response.json().then(data => {
                setParticipants(data);
              });
            }
          })
    }
  }

  function Participants({ participants }){
    if (!participants) {
      return;
    }
    const rows=[];
    participants.forEach((p) => {
      rows.push(
        <tr key={p.id}>
          <td>{p.name}</td>
          <td>{p.code}</td>
        </tr>
      );
    });
    return(
      <table>
        {rows}
      </table>
    );
  }

  useEffect(() => {
    loadMethods();
    console.log("Loading event", searchParams);
    const eventId = searchParams.get("id");
    loadEvent(eventId);
    loadParticipants(eventId);
  }, []);

  const handleSubmit = e => {
    e.preventDefault();
    const data = selectedPersonType === "LEGAL"
        ? {
          type: "LEGAL",
          name: jPName,
          code: jPCode,
          count: jPCount,
          paymentType: jPMethod,
          info: jPInfo
        } : {
          type: "PHYSICAL",
          firstName: pPFirstName,
          lastName: pPLastName,
          code: pPCode,
          paymentType: pPMethod,
          info: pPInfo
        };

    const url = EVENTS_URL + "event/" + event.id + "/participants";
    const options = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    };
    fetch(url, options)
        .then(response => {
          if (response.ok) {
            loadParticipants(event.id);
          }
        });
  }

  const handlePersonTypeChange = e => {
    const type = e.target.value;
    console.log("Handling person type change", type);
    setSelectedPersonType(type);
  }

  function PhysicalForm() {
    if (selectedPersonType === "PHYSICAL") {
      return(
        <form onSubmit={handleSubmit}>
          <table>
            <tr>
              <td><label for="ppfirstname">Eesnimi:</label></td>
              <td><input id="ppfirstname" name="ppfirstname" type="text"
                  value={pPFirstName || ""}
                  onChange={e => setPPFirstName(e.target.value)}/></td>
            </tr>
            <tr>
              <td><label for="pplastname">Perenimi:</label></td>
              <td><input id="pplastname" name="pplastname" type="text"
                  value={pPLastName || ""}
                  onChange={e => setPPLastName(e.target.value)} /></td>
            </tr>
            <tr>
              <td><label for="ppcode">Isikukood:</label></td>
              <td><input id="ppcode" name="ppcode" type="text"
                  value={pPCode || ""}
                  onChange={e => setPPCode(e.target.value)} /></td>
            </tr>
            <tr>
              <td><label for="ppmethod">Maksmisviis:</label></td>
              <td><select id="ppmethod" name="ppmethod"
                  defaultValue={pPMethod}
                  onChange={e => setPPMethod(e.target.value)}>
                {paymentTypes.map((opt, i) => <option value={opt.code}>{opt.name}</option>)}
                </select></td>
            </tr>
            <tr>
              <td><label for="ppinfo">Lisainfo:</label></td>
              <td><textarea id="ppinfo" name="ppinfo"
                  value={pPInfo || ""}
                  onChange={e => setPPInfo(e.target.value)} /></td>
            </tr>
          </table>
          <input type="submit" value="Lisa" />
        </form>
      );
    }
  }

  function JuridicalForm() {
    if (selectedPersonType === "LEGAL") {
      return(
        <form onSubmit={handleSubmit}>
          <table>
            <tr>
              <td><label for="jptname">Juriidiline nimi:</label></td>
              <td><input id="jpname" name="jpname" type="text"
                  value={jPName || ""}
                  onChange={e => setJPName(e.target.value)}/></td>
            </tr>
            <tr>
              <td><label for="jpcode">Registrikood:</label></td>
              <td><input id="jpcode" name="jpcode" type="text"
                  value={jPCode || ""}
                  onChange={e => setJPCode(e.target.value)} /></td>
            </tr>
            <tr>
              <td><label for="jpcount">Osavõtjate arv:</label></td>
              <td><input id="jpcount" name="jpcount" type="number"
                  value={jPCount || ""}
                  onChange={e => setJPCount(e.target.value)} /></td>
            </tr>
            <tr>
              <td><label for="jpmethod">Maksmisviis:</label></td>
              <td><select id="jpmethod" name="jpmethod"
                  defaultValue={jPMethod}
                  onChange={e => setJPMethod(e.target.value)}>
                {paymentTypes.map((opt, i) => <option value={opt.code}>{opt.name}</option>)}
                </select></td>
            </tr>
            <tr>
              <td><label for="jpinfo">Lisainfo:</label></td>
              <td><textarea id="jpinfo" name="jpinfo"
                  value={jPInfo || ""}
                  onChange={e => setJPInfo(e.target.value)} /></td>
            </tr>
          </table>
          <input type="submit" value="Lisa" />
        </form>
      );
    }
  }

  return(
    <div>
      <h2>Osavõtjad</h2>
      <table>
        <tr>
          <td>Ürituse nimi:</td>
          <td>{eventName}</td>
        </tr>
        <tr>
          <td>Toimumisaeg:</td>
          <td>{eventTime}</td>
        </tr>
        <tr>
          <td>Koht:</td>
          <td>{eventPlace}</td>
        </tr>
        <td>
          Osavõtjad:
        </td>
        <td>
          <Participants participants={participants} />
        </td>
      </table>
      <h2>Osavõtjate lisamine</h2>
      <label><input type="radio" name="personType" value="PHYSICAL"
          checked={selectedPersonType === "PHYSICAL"}
          onChange={handlePersonTypeChange}/>Eraisik</label>
      <label><input type="radio" name="personType" value="LEGAL"
          checked={selectedPersonType === "LEGAL"}
          onChange={handlePersonTypeChange} />Ettevõte</label>
      <PhysicalForm />
      <JuridicalForm />
    </div>
  );
}

export default App;

