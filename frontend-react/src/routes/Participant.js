import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { EVENTS_URL, validateIdCode } from "../constants";


function App() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [eventId, setEventId] = useState(searchParams.get("eventId"));
  const [personType, setPersonType] = useState(searchParams.get("type"));
  const [participant, setParticipant] = useState(null);
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

  async function loadParticipant(type, id) {
    if (id) {
      const url = EVENTS_URL + "participant/" + type + "/" + id;
      console.log("Fetching participant", url);
      const options = {
          method: "GET",
          headers: { "Content-Type": "application/json" }
      }
      fetch(url, options)
          .then(response => {
            if (response.ok) {
              response.json().then(data => {
                setParticipant(data);
                console.log("Loaded person", data);
                setPersonType(data.type);
              });
            }
          });
    }
  }

  const handleBackNav = e => {
    e.preventDefault();
    navigate("/participants?id=" + searchParams.get("eventId"));
  }

  useEffect(() => {
    loadMethods();
    console.log("Loading participant", searchParams);
    const participantType = searchParams.get("type");
    const participantId = searchParams.get("id");
    loadParticipant(participantType, participantId);
  }, []);

  function ParticipantAddingForm({ person }) {
    const [selectedPersonType, setSelectedPersonType] = useState(person.type);
  
    const handlePersonTypeChange = e => {
      const type = e.target.value;
      console.log("Handling person type change", type);
      setSelectedPersonType(type);
    }
  
    console.log("ParticipantAddingForm", selectedPersonType);
    return(
      <div className="content">
        <h2>Osavõtja info</h2>
        <label><input type="radio" name="personType" value="PHYSICAL" disabled="true"
            checked={selectedPersonType === "PHYSICAL"}
            onChange={handlePersonTypeChange}/>Eraisik</label>
        <label><input type="radio" name="personType" value="LEGAL" disabled="true"
            checked={selectedPersonType === "LEGAL"}
            onChange={handlePersonTypeChange} />Ettevõte</label>
        <PhysicalForm visible={selectedPersonType === "PHYSICAL"} />
        <JuridicalForm visible={selectedPersonType === "LEGAL"} />
      </div>
    );
  }

  function PhysicalForm({ visible }) {
    const [pPFirstName, setPPFirstName] = useState(participant.firstName);
    const [pPLastName, setPPLastName] = useState(participant.lastName);
    const [pPCode, setPPCode] = useState(participant.code);
    const [pPMethod, setPPMethod] = useState(participant.paymentType);
    const [pPInfo, setPPInfo] = useState(participant.info);
    const [errorMessage, setErrorMessage] = useState(null);

    const handleSubmit = e => {
      e.preventDefault();
      if (!validateIdCode(pPCode)) {
        console.log("Invalid personal code: ", pPCode);
        setErrorMessage("Isikukood ei sobi: " + pPCode);
        return;
      }
      const data = {
        type: "PHYSICAL",
        firstName: pPFirstName,
        lastName: pPLastName,
        code: pPCode,
        paymentType: pPMethod,
        info: pPInfo
      };

      const url = EVENTS_URL + "participant/" + personType + "/" + searchParams.get('id');
      const options = {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      };
      fetch(url, options)
          .then(response => {
            if (response.ok) {
              loadParticipant(personType, searchParams.get("id"));
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
          <button onClick={handleBackNav}>Tagasi</button> <input type="submit" value="Salvesta" />
          {errorMessage && <div class="error">{errorMessage}</div>}
        </form>
      );
    }
  }

  function JuridicalForm({ visible }) {
    const [jPName, setJPName] = useState(participant.name);
    const [jPCode, setJPCode] = useState(participant.code);
    const [jPCount, setJPCount] = useState(participant.count);
    const [jPMethod, setJPMethod] = useState(participant.paymentType);
    const [jPInfo, setJPInfo] = useState(participant.info);

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

      const url = EVENTS_URL + "participant/" + personType + "/" + searchParams.get('id');
      const options = {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      };
      fetch(url, options)
          .then(response => {
            if (response.ok) {
              loadParticipant(personType, searchParams.get("id"));
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
          <button onClick={handleBackNav}>Tagasi</button> <input type="submit" value="Salvesta" />
        </form>
      );
    }
  }

  if (participant) {
    return(
      <ParticipantAddingForm person={participant} />
    )
  }
}

export default App;
