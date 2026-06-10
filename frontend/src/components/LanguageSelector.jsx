function LanguageSelector(props) {

    return (

        <select
            value={props.language}
            onChange={(e) =>
                props.setLanguage(
                    e.target.value
                )
            }
        >

            <option value="java">
                ☕ Java
            </option>

            <option value="python">
                🐍 Python
            </option>

            <option value="cpp">
                ⚙️ C++
            </option>

        </select>

    );
}

export default LanguageSelector;