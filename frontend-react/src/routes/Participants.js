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
  const [jPFirstName, setJPFirstName] = useState("");
  const [jPLastName, setJPLastName] = useState("");
  const [jPCode, setJPCode] = useState("");
  const [jPMethod, setJPMethod] = useState("");
  const [jPInfo, setJPInfo] = useState("");
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
    const url = EVENTS_URL + "event/" + event.id + "/participants";
    const options = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        type: "PHYSICAL",
        firstName: jPFirstName,
        lastName: jPLastName,
        code: jPCode,
        paymentType: jPMethod,
        info: jPInfo
      })
    };
    fetch(url, options)
        .then(response => {
          if (response.ok) {
            loadParticipants(event.id);
          }
        });
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
      <form onSubmit={handleSubmit}>
        <table>
          <tr>
            <td><label for="jpfirstname">Eesnimi:</label></td>
            <td><input id="jpfirstname" name="jpfirstname" type="text"
                value={jPFirstName || ""}
                onChange={e => setJPFirstName(e.target.value)}/></td>
          </tr>
          <tr>
            <td><label for="jplastname">Perenimi:</label></td>
            <td><input id="jplastname" name="jplastname" type="text"
                value={jPLastName || ""}
                onChange={e => setJPLastName(e.target.value)} /></td>
          </tr>
          <tr>
            <td><label for="jpcode">Isikukood:</label></td>
            <td><input id="jpcode" name="jpcode" type="text"
                value={jPCode || ""}
                onChange={e => setJPCode(e.target.value)} /></td>
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
    </div>
  );
}

export default App;

