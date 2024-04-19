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
  const [paymentTypes, setPaymentTypes] = useState([]);

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

  const handleBackNav = e => {
    e.preventDefault();
    navigate("/");
  }

  function Participants({ participants }){
    const handleDelete = e => {
      if (!window.confirm("Oled kindel, et soovid osalejat eemaldada?")) {
        return;
      }
        const pId = e.currentTarget.getAttribute("participant-id");
      const pType = e.currentTarget.getAttribute("participant-type");
      console.log("Deleting", pId, pType);
      const url = EVENTS_URL + "event/" + event.id + "/participant/" + pType + "/" + pId;
      const options = {
          method: "DELETE",
          headers: { "Content-Type": "application/json" }
      }
      fetch(url, options)
          .then(response => {
            if (response.ok) {
              loadParticipants(event.id);
            }
          });
    }
    if (!participants) {
      return;
    }
    const rows=[];
    participants.forEach((p) => {
      rows.push(
        <tr key={p.id}>
          <td>{p.name}</td>
          <td>{p.code}</td>
          <td><button onClick={handleDelete} participant-type={p.type} participant-id={p.id}>
            <img src="remove.svg" width="20" height="20" alt="Kustuta" />
            </button></td>
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

  function ParticipantAddingForm() {
    const [selectedPersonType, setSelectedPersonType] = useState("PHYSICAL");
  
    const handlePersonTypeChange = e => {
      const type = e.target.value;
      console.log("Handling person type change", type);
      setSelectedPersonType(type);
    }
  
    console.log("ParticipantAddingForm", selectedPersonType);
    return(
      <>
      <h2>Osavõtjate lisamine</h2>
      <label><input type="radio" name="personType" value="PHYSICAL"
          checked={selectedPersonType === "PHYSICAL"}
          onChange={handlePersonTypeChange}/>Eraisik</label>
      <label><input type="radio" name="personType" value="LEGAL"
          checked={selectedPersonType === "LEGAL"}
          onChange={handlePersonTypeChange} />Ettevõte</label>
      <PhysicalForm visible={selectedPersonType === "PHYSICAL"} />
      <JuridicalForm visible={selectedPersonType === "LEGAL"} />
      </>
    );
  }

  function PhysicalForm({ visible }) {
    const [pPFirstName, setPPFirstName] = useState("");
    const [pPLastName, setPPLastName] = useState("");
    const [pPCode, setPPCode] = useState("");
    const [pPMethod, setPPMethod] = useState("TRANSFER");
    const [pPInfo, setPPInfo] = useState("");

    const handleSubmit = e => {
      e.preventDefault();
      const data = {
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

    console.log("PhysicalForm", visible);
    if (visible) {
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
          <button onClick={handleBackNav}>Tagasi</button> <input type="submit" value="Lisa" />
        </form>
      );
    }
  }

  function JuridicalForm({ visible }) {
    const [jPName, setJPName] = useState("");
    const [jPCode, setJPCode] = useState("");
    const [jPCount, setJPCount] = useState(1);
    const [jPMethod, setJPMethod] = useState("TRANSFER");
    const [jPInfo, setJPInfo] = useState("");

    const handleSubmit = e => {
      e.preventDefault();
      const data = {
        type: "LEGAL",
        name: jPName,
        code: jPCode,
        count: jPCount,
        paymentType: jPMethod,
        info: jPInfo
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

    console.log("JuridicalForm", visible);
    if (visible) {
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
          <button onClick={handleBackNav}>Tagasi</button> <input type="submit" value="Lisa" />
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
      <ParticipantAddingForm />
    </div>
  );
}

export default App;

