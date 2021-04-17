import React, { useCallback, useEffect, useRef, useState } from "react";
import { hot } from "react-hot-loader";
import { Pokemon, UserDex } from "../types/user";
import { FixedSizeList as List } from "react-window";
import {
  Divider,
  Hidden,
  Input,
  InputAdornment,
  TextField,
  Typography,
} from "@material-ui/core";
import { Search } from "@material-ui/icons";
import Row from "./Dex/Row";
import useStyles from "./Dex/styles";
import classNames from "classnames";
import GridRow from "./Row";
import GridColumn from "./Column";
import { applyChanges } from "../functions/my-dex";
import store from "../store";
import { updatePokedex as updateUserDex } from "../actions/session";
import EditIcon from "@material-ui/icons/Edit";
import CheckCircleIcon from "@material-ui/icons/CheckCircle";
import { caughtPokemon, updateDexName } from "../actions/syncQueue";

type DexProps = {
  dex: UserDex;
};

const Dex = (props: DexProps) => {
  const changes = useRef<Array<number>>(
    props.dex.pokemon.filter((p) => p.caught).map((p) => p.dexNumber)
  );

  const handleChange = (b: boolean, n: number) => {
    if (b && !changes.current.includes(n)) {
      changes.current.push(n);
    } else if (!b && changes.current.includes(n)) {
      changes.current = changes.current.filter((n) => n != n);
    }
  };

  const classes = useStyles();

  const [search, setSearch] = useState<string | undefined>(undefined);
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(props.dex.name);

  const shouldRender = useCallback(
    (p: Pokemon) =>
      search == undefined ||
      search == "" ||
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.dexNumber.toString().includes(search),
    [search]
  );

  const togglePokemonCaught = (index: number) => {
    const item = items[index - 1]; // -1 because we are indexing by pokedex id, which starts at 1
    const newItems = items.concat();
    newItems[index - 1] = {
      ...item,
      caught: !item.caught,
    };

    setItems(newItems);
    handleChange(!item.caught, index);
  };
  const [items, setItems] = useState(props.dex.pokemon);
  const itemData = {
    items,
    togglePokemonCaught,
    displayedItems: items.filter(shouldRender),
    dexId: props.dex.userDexId,
  };

  const handleSearchChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) =>
      setSearch(event.currentTarget.value),
    []
  );

  useEffect(() => {
    return () => {
      store.dispatch(
        updateUserDex(props.dex.userDexId, applyChanges(changes.current))
      );
      if (changes.current.length > 0) {
        store.dispatch(caughtPokemon(props.dex.userDexId, changes.current));
      }
    };
  }, [props.dex.userDexId]);

  return (
    <div className={classes.root}>
      <Typography variant="h5">
        <div className={classNames("bold", "center-v", "pb-1")}>
          <span className={`pokemon pokesprite bulbasaur pt-1`} />
          <span style={{ paddingBottom: "3px" }}>
            {!isEditing && (props.dex.name || props.dex.game.displayName)}
            {isEditing && (
              <TextField
                onChange={(e) => setName(e.target.value)}
                value={name}
              />
            )}
          </span>
          <span className="pl-1 2 cursor-pointer">
            {!isEditing && <EditIcon onClick={() => setIsEditing(true)} />}
            {isEditing && (
              <CheckCircleIcon
                color="action"
                onClick={() => {
                  setIsEditing(false);
                  store.dispatch(
                    updateDexName({ name: name!, dexId: props.dex.userDexId })
                  );
                }}
              />
            )}
          </span>
        </div>
      </Typography>
      <GridRow className={classNames("ml-2", "mr-2")}>
        <GridColumn xs={7} md={8}>
          <div>
            <Typography
              variant="h6"
              color="textSecondary"
              className={classNames("capitalize")}
            >
              {props.dex.type.toLowerCase()}
            </Typography>
          </div>
        </GridColumn>
        <GridColumn xs={5} md={4} className="center">
          <Typography
            className={classNames(classes.secondaryHeading, "pr-1 pr-md-0")}
          >
            <span id="counter">{items.filter((p) => p.caught).length}</span>/
            {items.length}
          </Typography>
        </GridColumn>
      </GridRow>
      <GridRow style={{ height: "72px" }}>
        <Hidden smDown>
          <GridColumn xs={3} md={1} className="center" />
        </Hidden>
        <Hidden smDown>
          <GridColumn
            md={1}
            className={classNames("center", "bold", classes.listItem)}
          >
            <Typography variant="button" style={{ fontSize: "14px" }}>
              Number
            </Typography>
          </GridColumn>
        </Hidden>
        <GridColumn
          xs={9}
          md={8}
          className={classNames(
            "center-v",
            "bold",
            "pl-3",
            "pl-md-0",
            classes.listItem
          )}
        >
          <Input
            fullWidth
            placeholder="Bulbasaur"
            value={search}
            onChange={handleSearchChange}
            endAdornment={
              <InputAdornment position="end">
                <Search />
              </InputAdornment>
            }
          />
        </GridColumn>
        <GridColumn
          xs={3}
          md={1}
          className={classNames("center", "bold", classes.listItem)}
        >
          <Typography variant="button" style={{ fontSize: "14px" }}>
            Caught
          </Typography>
        </GridColumn>
      </GridRow>
      <Divider />
      <Hidden smDown>
        <List
          height={720}
          itemCount={items.filter(shouldRender).length}
          itemData={itemData}
          itemSize={72}
          width={"100%"}
        >
          {Row}
        </List>
      </Hidden>
      <Hidden mdUp>
        <List
          height={320}
          itemCount={items.filter(shouldRender).length}
          itemData={itemData}
          itemSize={72}
          width={"100%"}
        >
          {Row}
        </List>
      </Hidden>
    </div>
  );
};

export default hot(module)(Dex);
