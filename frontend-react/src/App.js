import * as react from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';

const Home = react.lazy(() => import("./routes/Home"));
const Event = react.lazy(() => import("./routes/Event"));
const Participant = react.lazy(() => import("./routes/Participant"));
const Participants = react.lazy(() => import("./routes/Participants"));

function App() {
  return (
    <Router>
      <div className="App">
        <table className='menu'>
          <tr>
            <td className='logo'><img src="logo.svg" height="40" width="140" alt="NULLAM logo" /></td>
            <td className='menu'>
              <ul className='menu'>
                <li><Link to="/">AVALEHT</Link></li>
                <li><Link to="/event">ÜRITUSE&nbsp;LISAMINE</Link></li>
              </ul>
            </td>
            <td><img src="symbol.svg" height="60" width="60" alt="Sümbol" /></td>
          </tr>
        </table>
        <div className="filler">
          <div>
            Sed nec elit vestibulum, <strong>tincidunt orci</strong> et, sagittis
            ex. Vestibulum rutrum <strong>neque sucipit</strong> ante
            mattis maximus. Nulla non sapien <strong>viverra,</strong>
            <strong>lobotis lorem non, </strong> accumsan metus.
          </div>
          <img src="pilt.jpg" alt="Tool puu all" />
        </div>
        <div className='main'>
          <react.Suspense fallback={<>Loading...</>}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/event" element={<Event />} />
              <Route path="/participant" element={<Participant />} />
              <Route path="/participants" element={<Participants />} />
            </Routes>
          </react.Suspense>
        </div>
        <div className='footer'>
          <div className="fcontent">
          <table>
            <tr>
              <td>
                <h3>Curabitur</h3>
                Emauris<br />
                Kfringilla<br />
                Oin magna sem<br />
                Kelementum
              </td>
              <td>
                <h3>Fusce</h3>
                Econsectetur<br />
                Ksollicitudin<br />
                Omvulputate<br />
                Nunc fringilla tellu
              </td>
              <td>
                <h3>Kontakt</h3>
                <table>
                  <thead>
                    <tr>
                      <th>Peakontor: Tallinnas</th>
                      <th>Harukontor: Võrus</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>Väike-Ameerika 1, 11415 Tallinn</td>
                      <td>Oja tn 7 (külastusaadress)</td>
                    </tr>
                    <tr>
                      <td>Telefon: 605 4450</td>
                      <td>Telefon: 605 3330</td>
                    </tr>
                    <tr>
                      <td>Faks: 605 3186</td>
                      <td>Faks: 605 3155</td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
          </table>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;
